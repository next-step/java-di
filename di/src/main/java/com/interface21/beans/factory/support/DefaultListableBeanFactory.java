package com.interface21.beans.factory.support;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.context.stereotype.Component;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitionReader beanDefinitionReader;

    private final Map<Class<?>, Object> singletonObjects = new ConcurrentHashMap<>();

    public DefaultListableBeanFactory(String[] basePackages) {
        this.beanDefinitionReader = new AnnotationBeanDefinitionReader(basePackages);
    }

    public void initialize() {
        beanDefinitionReader.loadBeanDefinitions(new Class[] {Component.class});
        beanDefinitionReader
                .getBeanDefinitions()
                .forEach(
                        beanDefinition -> {
                            Class<?> beanClazz = beanDefinition.getType();
                            singletonObjects.put(beanClazz, getBeanOrCreate(beanClazz));
                        });
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionReader.getBeanClasses();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return (T) singletonObjects.get(clazz);
    }

    @Override
    public Object getBeanOrCreate(Class<?> clazz) {
        Class<?> concreteClazz =
                BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses())
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "No concrete class found for " + clazz.getName()));

        Object instance = singletonObjects.get(concreteClazz);
        if (instance != null) {
            return instance;
        }
        Constructor<?> constructor = ConstructorResolver.resolveConstructor(concreteClazz);
        Object[] arguments = ConstructorArgumentResolver.resolveConstructorArguments(constructor, this);
        return registerBean(constructor, arguments);
    }

    private Object registerBean(Constructor<?> constructor, Object[] arg) {
        try {
            return addBean(constructor.newInstance(arg));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object addBean(Object bean) {
        log.info("Bean [{}] is being registered", bean.getClass().getSimpleName());

        singletonObjects.put(bean.getClass(), bean);
        return bean;
    }

    @Override
    public void clear() {}
}
