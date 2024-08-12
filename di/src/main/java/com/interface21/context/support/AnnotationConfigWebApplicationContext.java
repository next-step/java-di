package com.interface21.context.support;

import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.beans.factory.support.BeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final Class<?>... configurationClasses) {
        this.beanFactory = new DefaultListableBeanFactory();
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(configurationClasses);
        final var scanner = new BeanScanner(beanFactory);
        scanner.scan(BeanFactoryUtils.getBasePackages(configurationClasses));
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

    @Override
    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return beanFactory.getBeansWithAnnotation(annotationType);
    }
}
