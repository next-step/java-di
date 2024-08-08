package com.interface21.beans.factory.config;

public class SingletonBeanDefinition implements BeanDefinition {
    private final Class<?> type;

    public SingletonBeanDefinition(Class<?> type) {
        this.type = type;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getBeanClassName() {
        return type.getName();
    }
}
