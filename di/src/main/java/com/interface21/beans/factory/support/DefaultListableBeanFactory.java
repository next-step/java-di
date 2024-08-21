package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.BeanDefinitions;
import com.interface21.beans.factory.config.SingletonRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);
    private final BeanDefinitions beanDefinitions;
    private final SingletonRegistry singletonRegistry;
    private final Set<Class<?>> tempBeansInCreation;

    public DefaultListableBeanFactory(BeanDefinitions beanDefinitions) {
        this.beanDefinitions = beanDefinitions;
        this.singletonRegistry = new SingletonRegistry();
        this.tempBeansInCreation = new HashSet<>();
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitions.getBeanClasses();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return clazz.cast(singletonRegistry.getSingleton(clazz));
    }

    public void initialize() {
        getBeanClasses().forEach(this::initBean);
    }

    private Object initBean(final Class<?> beanClass) {
        if (singletonRegistry.isContainsKey(beanClass)) {
            return singletonRegistry.getSingleton(beanClass);
        }

        tempBeansInCreation.add(beanClass);

        final Object createdBean = createBean(beanDefinitions.getBeanDefinition(beanClass));
        singletonRegistry.put(beanClass, createdBean);

        tempBeansInCreation.remove(beanClass);
        return createdBean;
    }

    private Object createBean(final BeanDefinition beanDefinition) {
        try {
            return beanDefinition.createBean(this::getOrCreateBean);
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(beanDefinition.getType(), e.getMessage(), e);
        }
    }

    private Object getOrCreateBean(Class<?> parameterType) {
        if (singletonRegistry.isContainsKey(parameterType)) {
            return singletonRegistry.getSingleton(parameterType);
        }
        return initBean(parameterType);
    }

    @Override
    public void clear() {
        beanDefinitions.clear();
        singletonRegistry.clear();
        tempBeansInCreation.clear();
    }
}
