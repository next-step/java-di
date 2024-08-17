package com.interface21.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.interface21.beans.BeanInstantiationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.context.stereotype.Component;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitionReader beanDefinitionReader;
    private final BeanRegistry beanRegistry;

    public DefaultListableBeanFactory(String[] basePackages) {
        this.beanDefinitionReader = new AnnotationBeanDefinitionReader(basePackages);
        this.beanRegistry = new DefaultBeanRegistry();
    }

    public void initialize() {
        beanDefinitionReader.loadBeanDefinitions(new Class[] {Component.class});
        beanDefinitionReader
                .getBeanDefinitions()
                .forEach(
                        beanDefinition -> {
                            Class<?> beanClazz = beanDefinition.getType();
                            beanRegistry.registerBean(getBeanOrCreate(beanClazz));
                        });
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionReader.getBeanClasses();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return (T) beanRegistry.getBean(clazz);
    }

    @Override
    public Object getBeanOrCreate(Class<?> clazz) {
        Class<?> concreteClazz =
                BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses())
                        .orElseThrow(() -> new BeanClassNotFoundException(clazz.getSimpleName()));

        if (beanRegistry.registeredBean(concreteClazz)) {
            return beanRegistry.getBean(concreteClazz);
        }
        Constructor<?> constructor = ConstructorResolver.resolveConstructor(concreteClazz);
        Object[] arguments = ConstructorArgumentResolver.resolveConstructorArguments(constructor, this);
        return instantiateBean(constructor, arguments);
    }

    @Override
    public void clear() {

    }

    private Object instantiateBean(Constructor<?> constructor, Object[] arg) {
        try {
            return constructor.newInstance(arg);
        } catch ( InstantiationException
                  | IllegalAccessException
                  | IllegalArgumentException
                  | InvocationTargetException
                  | IllegalStateException e) {
            throw new BeanInstantiationException(constructor, "Failed to instantiate bean [%s]".formatted(constructor.getDeclaringClass().getSimpleName()), e);
        }
    }
}
