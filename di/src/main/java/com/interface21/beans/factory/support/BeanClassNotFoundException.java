package com.interface21.beans.factory.support;

public class BeanClassNotFoundException extends RuntimeException {
    public BeanClassNotFoundException(String beanName) {
        super("Bean not found: [%s]".formatted(beanName));
    }
}
