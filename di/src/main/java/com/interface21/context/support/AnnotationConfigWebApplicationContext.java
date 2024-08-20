package com.interface21.context.support;

import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;

import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    public AnnotationConfigWebApplicationContext(final String... basePackages) {
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
