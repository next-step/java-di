package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.stereotype.Component;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private static final String STEREOTYPE_PACKAGE = "com.interface21.context.stereotype";

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();
    private final String[] basePackages;

    public DefaultListableBeanFactory(String... basePackages) {
        this.basePackages = basePackages;
    }


    @Override
    public Set<Class<?>> getBeanClasses() {
        return Set.of();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return (T) singletonObjects.get(clazz);
    }

    public void initialize() {
        log.info("initialize DefaultListableBeanFactory");
        Reflections reflections = new Reflections(STEREOTYPE_PACKAGE, basePackages);
        List<Class<?>> componentClasses = reflections.getTypesAnnotatedWith(Component.class)
                .stream()
                .filter(Predicate.not(Class::isAnnotation))
                .toList();

        for (Class<?> clazz : componentClasses) {
            doDependencyInjection(clazz, componentClasses);
        }
    }

    private Object doDependencyInjection(Class<?> clazz, List<Class<?>> componentClasses) {
        // 클래스들에서 @Autowired가 달려있는 클래스 생성자 하나를 탐색
        Constructor constructor = findAutoWiredConstructor(clazz, componentClasses);

        // 생성자를 돌면서 인스턴스화 및 등록
        // 생성자 파라미터가 Bean으로 등록된 경우 그대로 사용
        // 생성자 파라미터가 Bean으로 등록되지 않은 경우 생성 및 등록 후 사용
        Object[] constructorArgs = Arrays.stream(constructor.getParameters())
                .map(parameter -> {
                    Class<?> parameterType = parameter.getType();  // 인터페이스일 수 있다.
                    if (singletonObjects.containsKey(parameterType)) {
                        return singletonObjects.get(parameterType);
                    }
                    // 없으면 생성 및 등록해야 하는데, 파라미터에 대해서도 위 전 과정을 전부 수행해야 한다.
                    // 인터페이스는 구체 클래스로 수행해야 하는데..
                    return doDependencyInjection(parameterType, componentClasses);
                })
                .toArray();

        constructor.setAccessible(true);
        try {
            // 완성된 Bean들을 등록
            Object newInstance = constructor.newInstance(constructorArgs);
            singletonObjects.put(newInstance.getClass(), newInstance);
            return newInstance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor findAutoWiredConstructor(Class<?> clazz, List<Class<?>> componentClasses) {
        Class<?> aClass = clazz;
        if (clazz.isInterface()) {
            aClass = componentClasses.stream()
                    .filter(c -> {
                        Set<Class<?>> interfaces = Arrays.stream(c.getInterfaces())
                                .collect(Collectors.toSet());
                        return interfaces.contains(clazz);
                    })
                    .findAny()
                    .get();
        }

        Constructor<?> autowiredConstructor = BeanFactoryUtils.getAutowiredConstructor(aClass);
        if (autowiredConstructor == null) {
            return aClass.getDeclaredConstructors()[0];
        }

        return autowiredConstructor;
    }

    @Override
    public void clear() {
    }

    @Override
    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotationType) {
        return singletonObjects.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(annotationType))
                .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
    }
}
