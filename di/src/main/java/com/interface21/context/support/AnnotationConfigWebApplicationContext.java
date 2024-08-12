package com.interface21.context.support;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;
import java.util.Map;
import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final Class<?>... configurationClasses) {
        Map<Class<?>, BeanDefinition> componentBeanDefinitions = ComponentScanner.from(configurationClasses).scan();
        this.beanFactory = new DefaultListableBeanFactory(componentBeanDefinitions);
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
