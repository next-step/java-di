package com.interface21.context;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.ConfigurationClassBeanDefinitionReader;
import com.interface21.beans.factory.support.BeanDefinitionReader;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public class WebApplicationContext implements ApplicationContext, BeanFactory {

    private final DefaultListableBeanFactory beanFactory;
    private final BeanDefinitionReader reader;

    public WebApplicationContext(final String... basePackages) {
        beanFactory = new DefaultListableBeanFactory(basePackages);
        reader = new ConfigurationClassBeanDefinitionReader(beanFactory);
    }

    public void initialize() {
        beanFactory.initialize();
        reader.loadBeanDefinitions(beanFactory.getBeanClasses().toArray(new Class[]{}));
        beanFactory.refresh();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public List<Object> getBeansForAnnotation(final Class<? extends Annotation> annotationType) {
        return beanFactory.getBeansForAnnotation(annotationType);
    }

    @Override
    public void clear() {
        beanFactory.clear();
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }
}
