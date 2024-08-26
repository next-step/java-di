package com.interface21.context.support;

import java.util.*;

import com.interface21.beans.factory.support.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.ApplicationContext;
import com.interface21.context.annotation.AnnotationConfigRegistry;

public class AnnotationConfigWebApplicationContext
        implements ApplicationContext, AnnotationConfigRegistry {

    private final Logger log = LoggerFactory.getLogger(AnnotationConfigWebApplicationContext.class);

    private final List<String> basePackages = new ArrayList<>();
    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(String basePackage) {
        Collections.addAll(this.basePackages, basePackage);
        this.beanFactory = new DefaultListableBeanFactory();
        scan(basePackages.toArray(String[]::new));
        initializeBeanFactory();
    }


    @Override
    public void scan(String[] basePackages) {
        var scanners = getBeanScanner(beanFactory);
        scanners.forEach(scanner -> scanner.scan(basePackages));
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }

    private void initializeBeanFactory() {
        this.beanFactory.initialize();
    }

    private List<BeanDefinitionScanner> getBeanScanner(BeanDefinitionRegistry beanFactory) {
        return List.of(
                new AnnotationBeanDefinitionScanner(beanFactory),
                new ConfigurationBeanDefinitionScanner(beanFactory));
    }
}
