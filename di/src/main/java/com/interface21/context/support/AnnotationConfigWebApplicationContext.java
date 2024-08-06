package com.interface21.context.support;

import java.util.Set;

import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final String... basePackages) {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return null;
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return Set.of();
    }
}
