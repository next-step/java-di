package com.interface21.beans.factory.config;

public interface BeanDefinition {

    Class<?> getType();

    String getName();

    boolean equalsType(Class<?> clazz);

    boolean isImplement(Class<?> clazz);
}
