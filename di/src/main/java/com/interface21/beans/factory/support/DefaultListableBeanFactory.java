package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private static final Random RANDOM = new Random();
    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

    @Override
    public Set<Class<?>> getBeanClasses() {
        return Set.of();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return null;
    }

    public void initialize() {
        for (BeanDefinition beanDefinition : beanDefinitionMap.values()) {
            createBean(beanDefinition);
        }
    }

    private void createBean(BeanDefinition beanDefinition) {
        if (singletonObjects.containsKey(beanDefinition.getType())) {
            return;
        }
        singletonObjects.put(beanDefinition.getType(), instantiateBean(beanDefinition));
    }

    private Object instantiateBean(BeanDefinition beanDefinition) {
        Constructor<?> targetConstructor = getTargetConstructor(beanDefinition.getType());
        return BeanUtils.instantiateClass(targetConstructor);
    }

    private Constructor<?> getTargetConstructor(Class<?> beanType) {
        Constructor<?>[] constructors = beanType.getConstructors();
        return Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findAny()
                .orElseGet(() -> randomConstructor(constructors));
    }

    private Constructor<?> randomConstructor(Constructor<?>[] constructors) {
        return constructors[RANDOM.nextInt(constructors.length)];
    }

    @Override
    public void clear() {
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        if (!isComponentPresent(clazz)) {
            throw new IllegalArgumentException("Component가 있는 클래스만 저장할 수 있습니다.");
        }
        beanDefinitionMap.put(clazz.getSimpleName(), beanDefinition);
    }

    private boolean isComponentPresent(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Component.class)) {
            return true;
        }
        return Arrays.stream(clazz.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(annotation -> annotation.isAnnotationPresent(Component.class));
    }

    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }

    public Map<Class<?>, Object> getSingletonObjects() {
        return singletonObjects;
    }
}
