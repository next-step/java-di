package com.interface21.context.support;

import com.interface21.beans.factory.support.BeanDefinitionRegistry;
import com.interface21.beans.factory.support.BeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;

import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final Class<?> applicationClass) {
        final BeanScanner beanScanners = new BeanScanner(applicationClass);
        final BeanDefinitionRegistry beanDefinitionRegistry = beanScanners.scan();

        this.beanFactory = new DefaultListableBeanFactory(beanDefinitionRegistry);
        beanFactory.initialize();
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
