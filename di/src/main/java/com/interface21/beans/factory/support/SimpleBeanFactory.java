package com.interface21.beans.factory.support;

import com.interface21.beans.NoSuchBeanDefinitionException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.context.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleBeanFactory implements BeanFactory {

    private final Map<Class<?>, Object> singletonObjects;

    public SimpleBeanFactory() {
        this.singletonObjects = new HashMap<>();
    }

    public void addBean(final Class<?> clazz, final Object bean) {
        this.singletonObjects.put(clazz, bean);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.keySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        return (T) singletonObjects.entrySet()
                .stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new NoSuchBeanDefinitionException(clazz));
    }

    @Override
    public void clear() {
        singletonObjects.clear();
    }

    @Override
    public Map<Class<?>, Object> getControllers() {
        return singletonObjects.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(Controller.class))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public boolean containsBean(final Class<?> parameterType) {
        return this.singletonObjects.containsKey(parameterType);
    }
}
