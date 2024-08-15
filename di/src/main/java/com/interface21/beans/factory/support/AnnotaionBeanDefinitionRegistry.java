package com.interface21.beans.factory.support;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.interface21.beans.factory.config.BeanDefinition;

public class AnnotaionBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private final Map<Class<?>, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitions.put(clazz, beanDefinition);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitions.keySet();
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return List.copyOf(beanDefinitions.values());
    }
}
