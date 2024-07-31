package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();
    private final String[] basePackages;

    public DefaultListableBeanFactory(String... basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(final Class<T> clazz) {
        return (T) singletonObjects.get(clazz);
    }

    public void initialize() {
        log.info("Start DefaultListableBeanFactory");
        BeanScanner beanScanner = new BeanScanner(basePackages);
        Set<Class<?>> beanClasses = beanScanner.scan();
        registerBeans(beanClasses);
    }

    private void registerBeans(Set<Class<?>> beanClasses) {
        for (Class<?> beanClass : beanClasses) {
            registerSingletonObject(beanClass, beanClasses);
        }
    }

    private Object registerSingletonObject(Class<?> beanClass, Set<Class<?>> beanClasses) {
        Constructor<?> constructor = findAutoWiredConstructor(beanClass, beanClasses);
        Object[] constructorArgs = getConstructorArgs(beanClasses, constructor);

        try {
            Object newInstance = createNewInstance(constructor, constructorArgs);
            singletonObjects.put(newInstance.getClass(), newInstance);
            return newInstance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> findAutoWiredConstructor(Class<?> clazz, Set<Class<?>> beanClasses) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, beanClasses)
                .orElseThrow(() -> new IllegalArgumentException("clazz는 beanClasses 내에 포함된 값이어야 합니다. clazz=%s, beanClasses=%s".formatted(clazz, beanClasses)));

        Constructor<?> autowiredConstructor = BeanFactoryUtils.getAutowiredConstructor(concreteClass);
        if (autowiredConstructor == null) {
            return concreteClass.getDeclaredConstructors()[0];
        }
        return autowiredConstructor;
    }

    private Object[] getConstructorArgs(Set<Class<?>> beanClasses, Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameters())
                .map(parameter -> {
                    Class<?> parameterType = parameter.getType();
                    if (singletonObjects.containsKey(parameterType)) {
                        return singletonObjects.get(parameterType);
                    }
                    return registerSingletonObject(parameterType, beanClasses);
                })
                .toArray();
    }

    private Object createNewInstance(Constructor<?> constructor, Object[] constructorArgs) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        constructor.setAccessible(true);
        return constructor.newInstance(constructorArgs);
    }

    @Override
    public void clear() {
        singletonObjects.clear();
    }

    @Override
    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotationType) {
        return singletonObjects.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(annotationType))
                .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
    }
}
