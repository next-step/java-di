package com.interface21.beans.factory.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;

public class DefaultListableBeanFactory implements BeanFactory {

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

    public void initialize() {}

    @Override
    public void clear() {}
}
