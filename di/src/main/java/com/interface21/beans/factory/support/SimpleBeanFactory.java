package com.interface21.beans.factory.support;

import com.interface21.beans.NoSuchBeanDefinitionException;
import com.interface21.beans.factory.BeanFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleBeanFactory implements BeanFactory {

    private final Map<String, Object> singletonObjects;

    public SimpleBeanFactory() {
        this.singletonObjects = new HashMap<>();
    }

    public void addBean(final String beanName, final Object bean) {
        if (singletonObjects.containsKey(beanName)) {
            throw new IllegalArgumentException("Bean " + beanName + " is already exist");
        }

        this.singletonObjects.put(beanName, bean);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.values().stream()
                .map(Object::getClass)
                .collect(Collectors.toSet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        return (T) singletonObjects.entrySet()
                .stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getValue().getClass()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new NoSuchBeanDefinitionException(clazz.getName()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(final String beanName) {
        return (T) singletonObjects.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(beanName))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new NoSuchBeanDefinitionException(beanName));
    }

    @Override
    public void clear() {
        singletonObjects.clear();
    }

    public boolean containsBean(final String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }
}
