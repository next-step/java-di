package com.interface21.beans.factory.config;

public class DefaultBeanDefinition implements BeanDefinition {
    private String beanClassName;
    private Class<?> type;

    public DefaultBeanDefinition(String beanClassName, Class<?> type) {
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
