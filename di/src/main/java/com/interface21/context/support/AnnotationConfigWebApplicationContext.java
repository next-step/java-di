package com.interface21.context.support;

import com.interface21.beans.factory.support.ClasspathBeanScanner;
import com.interface21.beans.factory.support.ConfigurationScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.beans.factory.support.Scanner;
import com.interface21.context.ApplicationContext;

import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final String... basePackages) {
        this.beanFactory = new DefaultListableBeanFactory();
        Scanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        Scanner configurationScanner = new ConfigurationScanner(beanFactory);

        classpathBeanScanner.scan(basePackages);
        configurationScanner.scan(basePackages);

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
