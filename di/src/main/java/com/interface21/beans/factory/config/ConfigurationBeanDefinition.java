package com.interface21.beans.factory.config;

import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements MethodBeanDefinition {

    private final Class<?> type;
    private final Method method;

    public ConfigurationBeanDefinition(Class<?> type, Method method) {
        this.type = type;
        this.method = method;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getBeanClassName() {
        return type.getName();
    }

    @Override
    public Method getMethod() {
        return method;
    }
}
