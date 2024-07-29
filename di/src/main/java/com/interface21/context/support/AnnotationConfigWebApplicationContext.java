package com.interface21.context.support;

import com.interface21.beans.factory.support.BeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;

import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final String... basePackages) {
        final BeanScanner beanScanner = new BeanScanner(basePackages);
        this.beanFactory = new DefaultListableBeanFactory(beanScanner.scan());
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
