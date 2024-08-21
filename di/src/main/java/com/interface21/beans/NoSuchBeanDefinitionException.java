package com.interface21.beans;

public class NoSuchBeanDefinitionException extends RuntimeException {

    public NoSuchBeanDefinitionException(final Class<?> clazz) {
        super("can not find bean of " + clazz.getName());
    }

}