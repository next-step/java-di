package com.interface21.context.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.context.ApplicationContext;

import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final BeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }
}
