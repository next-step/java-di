package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitions beanDefinitions;
    private final Beans beans;
    private final CircularReferenceSensor circularReferenceSensor;
    private final String[] basePackages;

    public DefaultListableBeanFactory(String... basePackages) {
        this.beanDefinitions = new BeanDefinitions();
        this.beans = new Beans();
        this.circularReferenceSensor = new CircularReferenceSensor();
        this.basePackages = basePackages;
    }

    public void initialize() {
        log.info("Start DefaultListableBeanFactory");
        BeanScanner beanScanner = new BeanScanner(basePackages);
        Set<Class<?>> beanClasses = beanScanner.scan();
        beanDefinitions.registerBeanDefinitions(beanClasses);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beans.getBeanClasses();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(final Class<T> clazz) {
        if (clazz == null || beanDefinitions.isNotRegistered(clazz)) {
            throw new IllegalArgumentException("빈으로 등록되지 않은 Class이거나 null입니다.");
        }

        if (beans.isRegistered(clazz)) {
            return beans.getBean(clazz);
        }

        T bean = (T) createBean(clazz);
        circularReferenceSensor.removeAllTargets();
        return bean;
    }

    private Object createBean(Class<?> clazz) {
        circularReferenceSensor.addTarget(clazz);

        Constructor<?> constructor = findAutoWiredConstructor(clazz);
        Object[] constructorArgs = getConstructorArgs(constructor);
        Object newBean = BeanUtils.instantiateClass(constructor, constructorArgs);
        beans.register(newBean);
        return newBean;
    }

    private Constructor<?> findAutoWiredConstructor(Class<?> clazz) {
        Set<Class<?>> beanTypes = beanDefinitions.extractTypes();
        Class<?> concreteClass = getConcreteClass(clazz, beanTypes);

        Constructor<?> autowiredConstructor = BeanFactoryUtils.getAutowiredConstructor(concreteClass);
        if (autowiredConstructor == null) {
            return concreteClass.getDeclaredConstructors()[0];
        }
        return autowiredConstructor;
    }

    private Object[] getConstructorArgs(Constructor<?> constructor) {
        List<? extends Class<?>> types = extractParameterTypes(constructor);
        detectCirculation(types);
        return types.stream()
                .map(this::getBean)
                .toArray();
    }

    private List<? extends Class<?>> extractParameterTypes(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameters())
                .map(Parameter::getType)
                .toList();
    }

    private void detectCirculation(List<? extends Class<?>> types) {
        types.forEach(type -> {
            Set<Class<?>> beanTypes = beanDefinitions.extractTypes();
            Class<?> concreteClass = getConcreteClass(type, beanTypes);
            circularReferenceSensor.detect(concreteClass);
        });
    }

    private Class<?> getConcreteClass(Class<?> clazz, Set<Class<?>> beanTypes) {
        return BeanFactoryUtils.findConcreteClass(clazz, beanTypes)
                .orElseThrow(() -> new IllegalArgumentException("인터페이스를 구현하는 구체 클래스가 없습니다. interface=%s, beanTypes=%s".formatted(clazz, beanTypes)));
    }

    @Override
    public void clear() {
        beans.clear();
    }

    @Override
    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotationType) {
        return beanDefinitions.extractTypes()
                .stream()
                .filter(type -> type.isAnnotationPresent(annotationType))
                .collect(Collectors.toUnmodifiableMap(type -> type, this::getBean));
    }
}
