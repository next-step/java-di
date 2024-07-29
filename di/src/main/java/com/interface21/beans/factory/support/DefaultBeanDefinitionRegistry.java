package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap;

    public DefaultBeanDefinitionRegistry() {
        this.beanDefinitionMap = new HashMap<>();
    }

    public DefaultBeanDefinitionRegistry(Map<String, BeanDefinition> beanDefinitionMap) {
        this.beanDefinitionMap = beanDefinitionMap;
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
}
