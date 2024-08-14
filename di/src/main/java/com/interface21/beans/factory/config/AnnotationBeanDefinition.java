package com.interface21.beans.factory.config;

public class AnnotationBeanDefinition implements BeanDefinition {

    private final Class<?> beanType;

    public AnnotationBeanDefinition(Class<?> beanType) {
        this.beanType = beanType;
    }

    @Override
    public Class<?> getType() {
        return beanType;
    }

    @Override
    public String getBeanClassName() {
        return beanType.getSimpleName();
    }
}
