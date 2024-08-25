package com.interface21.beans.factory.config;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public interface BeanDefinition {

    Class<?> getType();

    String getBeanClassName();

    Object createBean(Function<Class<?>, Object> beanSupplier)
        throws InvocationTargetException, IllegalAccessException, InstantiationException;
}
