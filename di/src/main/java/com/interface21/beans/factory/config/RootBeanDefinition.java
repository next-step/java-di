package com.interface21.beans.factory.config;

public class RootBeanDefinition implements BeanDefinition {

    private final Class<?> type;
    private final String beanClassName;

    public RootBeanDefinition(final Class<?> type, final String beanClassName) {
        this.type = type;
        this.beanClassName = beanClassName;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }
}
