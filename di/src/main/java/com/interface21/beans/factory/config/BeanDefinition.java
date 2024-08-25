package com.interface21.beans.factory.config;

import com.interface21.beans.factory.BeanFactory;

import java.lang.reflect.Constructor;

public interface BeanDefinition {

    Class<?> getType();

    String getBeanClassName();

    Constructor<?> getConstructor();

    Class<?>[] getParameterTypes();

    Object[] resolveArguments(BeanFactory beanFactory);
}
