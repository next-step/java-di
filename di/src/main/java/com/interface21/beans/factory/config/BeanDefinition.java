package com.interface21.beans.factory.config;

public interface BeanDefinition {

    Class<?> getType();

    String getBeanClassName();

    default boolean hasSameName(final BeanDefinition beanDefinition) {
        return getBeanClassName().equals(beanDefinition.getBeanClassName());
    }
}
