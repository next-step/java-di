package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

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
