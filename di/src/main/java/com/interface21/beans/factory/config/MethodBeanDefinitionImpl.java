package com.interface21.beans.factory.config;

import java.lang.reflect.Method;

public class MethodBeanDefinitionImpl implements BeanDefinition {
    private String beanClassName;
    private Class<?> type;
    private Method method;

    public MethodBeanDefinitionImpl(
            String beanClassName,
            Class<?> type,
            Method method
    ) {
        this.beanClassName = beanClassName;
        this.type = type;
        this.method = method;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getBeanClassName() {
        return beanClassName;
    }

    public Method getMethod() {
        return method;
    }
}
