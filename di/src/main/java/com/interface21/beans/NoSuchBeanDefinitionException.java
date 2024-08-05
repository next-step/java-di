package com.interface21.beans;

public class NoSuchBeanDefinitionException extends RuntimeException {

    public NoSuchBeanDefinitionException(final String beanName) {
        super("can not find bean of " + beanName);
    }

}
