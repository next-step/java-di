package com.interface21.beans.factory.config;

public class BeanDefinitionImpl implements BeanDefinition {
    private String beanClassName;
    private Class<?> type;

    public BeanDefinitionImpl(String beanClassName, Class<?> type) {
        this.beanClassName = beanClassName;
        this.type = type;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getBeanClassName() {
        return beanClassName;
    }
}
