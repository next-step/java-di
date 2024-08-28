package com.interface21.context.support;

import java.lang.annotation.Annotation;
import java.util.*;

import com.interface21.beans.factory.support.*;

import com.interface21.context.ApplicationContext;
import com.interface21.context.annotation.AnnotationConfigRegistry;

public class AnnotationConfigWebApplicationContext
        extends GenericApplicationContext
        implements ApplicationContext, AnnotationConfigRegistry {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(Class<?> configuration) {
        this.beanFactory = new DefaultListableBeanFactory();
        scan(configuration);
        initializeBeanFactory();
    }


    @Override
    public void scan(Class<?> configuration) {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(beanFactory);
        configurationBeanScanner.register(configuration);

        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        classpathBeanScanner.scan(BeanScanner.scanBasePackages(configuration));
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }

    @Override
    public Object[] getBeanWithAnnotation(Class<? extends Annotation> controllerClass) {
        return beanFactory.getBeanWithAnnotation(controllerClass);
    }

    private void initializeBeanFactory() {
        this.beanFactory.initialize();
    }

}
