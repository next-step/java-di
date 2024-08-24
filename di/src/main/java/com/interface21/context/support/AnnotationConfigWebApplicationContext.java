package com.interface21.context.support;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.support.BeanScanner;
import com.interface21.beans.factory.support.ConfigurationBeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.beans.factory.support.SimpleBeanDefinitionRegistry;
import com.interface21.context.ApplicationContext;

import java.util.Map;
import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(Class<?>... configurationClasses) {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(configurationClasses);
        Map<Class<?>, BeanDefinition> configurationBeanDefinitionMap = new ConfigurationBeanScanner(configurationClasses).scan();
        Map<Class<?>, BeanDefinition> genericBeanDefinitionMap = new BeanScanner(configurationBeanScanner.getBasePackages()).scan();

        final SimpleBeanDefinitionRegistry simpleBeanDefinitionRegistry = new SimpleBeanDefinitionRegistry();
        simpleBeanDefinitionRegistry.registerAll(configurationBeanDefinitionMap, genericBeanDefinitionMap);

        this.beanFactory = new DefaultListableBeanFactory(simpleBeanDefinitionRegistry);
        this.beanFactory.initialize();
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
    public Map<Class<?>, Object> getControllers() {
        return beanFactory.getControllers();
    }
}
