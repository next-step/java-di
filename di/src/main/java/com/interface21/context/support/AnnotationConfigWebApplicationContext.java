package com.interface21.context.support;

import com.interface21.beans.factory.config.BeanDefinitions;
import com.interface21.beans.factory.support.BeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;

import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final Class<?> applicationClass) {
        BeanScanner beanScanner = new BeanScanner(applicationClass);
        BeanDefinitions beanDefinitions = beanScanner.scan();
        this.beanFactory = new DefaultListableBeanFactory(beanDefinitions);
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
