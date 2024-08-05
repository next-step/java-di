package com.interface21.context.support;

import com.interface21.beans.factory.support.BeanDefinitionRegistry;
import com.interface21.beans.factory.support.BeanScanner;
import com.interface21.beans.factory.support.ConfigurationScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;

import java.util.List;
import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final Class<?> applicationClass) {
        final ConfigurationScanner configurationScanner = new ConfigurationScanner(List.of(applicationClass));
        final BeanScanner beanScanner = new BeanScanner(configurationScanner.getBasePackages());
        final BeanDefinitionRegistry beanDefinitionRegistry = beanScanner.scan();
        final BeanDefinitionRegistry configBeanDefinitionRegistry = configurationScanner.scanBean();
        beanDefinitionRegistry.mergeBeanDefinitionRegistry(configBeanDefinitionRegistry);
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
