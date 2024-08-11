package com.interface21.beans;

public class NoSuchBeanDefinitionException extends RuntimeException {

    private final Class<?> beanClass;

    public NoSuchBeanDefinitionException(Class<?> beanClass) {
        super("No bean named '" + beanClass.getName() + "' is defined");
        this.beanClass = beanClass;
    }

    public String getBeanName() {
        return beanClass.getName();
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }
}
