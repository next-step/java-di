package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    public void initialize() {
        log.info("Start DefaultListableBeanFactory");
        BeanScanner beanScanner = new BeanScanner(basePackages);
        Set<Class<?>> beanClasses = beanScanner.scan();
        registerBeanDefinitions(beanClasses);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(final Class<T> clazz) {
        if (singletonObjects.containsKey(clazz)) {
            return (T) singletonObjects.get(clazz);
        }

        Set<Class<?>> beanClasses = beanDefinitionMap.values()
                .stream()
                .map(BeanDefinition::getType)
                .collect(Collectors.toSet());

        return (T) registerSingletonObject(clazz, beanClasses);
    }

    private void registerBeanDefinitions(Set<Class<?>> beanClasses) {
        for (Class<?> beanClass : beanClasses) {
            DefaultBeanDefinition beanDefinition = new DefaultBeanDefinition(beanClass);
            beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
        }
    }

    private Object registerSingletonObject(Class<?> beanClass, Set<Class<?>> beanClasses) {
        Constructor<?> constructor = findAutoWiredConstructor(beanClass, beanClasses);
        Object[] constructorArgs = getConstructorArgs(constructor);
        Object newInstance = BeanUtils.instantiateClass(constructor, constructorArgs);
        singletonObjects.put(newInstance.getClass(), newInstance);
        return newInstance;
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

    private Object[] getConstructorArgs(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameters())
                .map(Parameter::getType)
                .map(this::getBean)
                .toArray();
    }

    @Override
    public void clear() {
        singletonObjects.clear();
    }

    @Override
    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotationType) {
        return beanDefinitionMap.values()
                .stream()
                .map(BeanDefinition::getType)
                .filter(type -> type.isAnnotationPresent(annotationType))
                .collect(Collectors.toUnmodifiableMap(type -> type, this::getBean));
    }
}
