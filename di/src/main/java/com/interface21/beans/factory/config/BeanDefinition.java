package com.interface21.beans.factory.config;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public interface BeanDefinition {

    Class<?> getType();

    String getBeanClassName();

    Object createBean(final Function<Class<?>, Object> beanSupplier) throws InvocationTargetException, IllegalAccessException, InstantiationException;

    default boolean hasSameName(final BeanDefinition beanDefinition) {
        return getBeanClassName().equals(beanDefinition.getBeanClassName());
    }
}