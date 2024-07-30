package com.interface21.context.support;

import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;

import com.interface21.context.stereotype.Controller;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final String... basePackages) {
        this.beanFactory = new DefaultListableBeanFactory(basePackages);
        beanFactory.initialize();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return null;
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return Set.of();
    }

    @Override
    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> controllerClass) {
        return beanFactory.getBeansAnnotatedWith(controllerClass);
    }
}
