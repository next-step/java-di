package com.interface21.beans.factory.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements BeanDefinition {
    private final Class<?> beanClass;
    private final Method beanMethod;

    private ConfigurationBeanDefinition(final Class<?> beanClass, final Method beanMethod) {
        this.beanClass = beanClass;
        this.beanMethod = beanMethod;
    }

    public static ConfigurationBeanDefinition from(final Method beanMethod) {
        return new ConfigurationBeanDefinition(beanMethod.getReturnType(), beanMethod);
    }

    @Override
    public Class<?> getType() {
        return beanClass;
    }

    @Override
    public String getBeanClassName() {
        return beanMethod.getName();
    }

    @Override
    public Constructor<?> getConstructor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Method getBeanMethod() {
        return beanMethod;
    }
}
