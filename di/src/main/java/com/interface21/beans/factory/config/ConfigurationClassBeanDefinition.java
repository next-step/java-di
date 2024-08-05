package com.interface21.beans.factory.config;

import java.lang.reflect.Method;

public class ConfigurationClassBeanDefinition implements BeanDefinition {

    private final Class<?> type;
    private final String beanClassName;
    private final Class<?> factoryBeanType;
    private final Method factoryMethod;

    public ConfigurationClassBeanDefinition(final Method method, final Class<?> factoryBeanType) {
        final Class<?> beanType = method.getReturnType();
        this.type = beanType;
        this.beanClassName = beanType.getName();
        this.factoryBeanType = factoryBeanType;
        this.factoryMethod = method;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public String getBeanClassName() {
        return beanClassName;
    }

    public Class<?> getFactoryBeanType() {
        return this.factoryBeanType;
    }

    public Method getFactoryMethod() {
        return this.factoryMethod;
    }
}
