package com.interface21.beans.factory.support;

public class BeanNotFoundException extends RuntimeException {
    public BeanNotFoundException(String beanName) {
        super("Bean not found: [%s]".formatted(beanName));
    }
}
