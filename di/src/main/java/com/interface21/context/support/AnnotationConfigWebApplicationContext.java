package com.interface21.context.support;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.factory.support.AnnotationBeanDefinitionScanner;
import com.interface21.beans.factory.support.BeanDefinitionRegistry;
import com.interface21.beans.factory.support.BeanDefinitionScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;
import com.interface21.context.annotation.AnnotationConfigRegistry;

public class AnnotationConfigWebApplicationContext
        implements ApplicationContext, AnnotationConfigRegistry {

    private final Logger log = LoggerFactory.getLogger(AnnotationConfigWebApplicationContext.class);

    private final List<String> basePackages = new ArrayList<>();
    private DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(String basePackage) {
        Collections.addAll(this.basePackages, basePackage);
    }

    public void refresh() {
        this.beanFactory = new DefaultListableBeanFactory();
        scan(basePackages.toArray(String[]::new));
        initializeBeans();
    }

    @Override
    public void scan(String[] basePackages) {
        BeanDefinitionScanner scanner = getBeanScanner(beanFactory);
        int count = scanner.scan(basePackages);

        log.info("{} beans found in {}", count, Arrays.toString(basePackages));
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }

    private void initializeBeans() {
        this.beanFactory.initialize();
    }

    private BeanDefinitionScanner getBeanScanner(BeanDefinitionRegistry beanFactory) {
        return new AnnotationBeanDefinitionScanner(beanFactory);
    }
}
