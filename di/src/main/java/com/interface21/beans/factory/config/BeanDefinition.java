package com.interface21.beans.factory.config;

public interface BeanDefinition {

    Class<?> getType();

    String getBeanClassName();

    BeanScope getScope();
}
