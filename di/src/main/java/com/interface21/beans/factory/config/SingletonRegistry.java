package com.interface21.beans.factory.config;

import com.interface21.beans.NoSuchBeanDefinitionException;

import java.util.HashMap;
import java.util.Map;

public class SingletonRegistry {
    private final Map<Class<?>, Object> singletonObjects;

    public SingletonRegistry(Map<Class<?>, Object> singletonObjects) {
        this.singletonObjects = singletonObjects;
    }

    public SingletonRegistry() {
        this.singletonObjects = new HashMap<>();
    }

    public Object getSingleton(Class<?> clazz) {
        return singletonObjects.entrySet()
                .stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new NoSuchBeanDefinitionException(clazz));
    }

    public void clear() {
        singletonObjects.clear();
    }

    public boolean isContainsKey(final Class<?> parameterType) {
        return singletonObjects.containsKey(parameterType);
    }

    public void put(final Class<?> beanClass, final Object createdBean) {
        singletonObjects.put(beanClass, createdBean);
    }
}
