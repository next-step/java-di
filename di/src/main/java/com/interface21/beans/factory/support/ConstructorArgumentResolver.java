package com.interface21.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import com.interface21.beans.factory.BeanFactory;

public class ConstructorArgumentResolver {

    public static Object[] resolveConstructorArguments(
            Constructor<?> constructor, BeanFactory beanFactory) {

        if (constructor.getParameterCount() == 0) {
            return new Object[0];
        }

        Parameter[] parameters = constructor.getParameters();

        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Class<?> parameterType = parameters[i].getType();
            args[i] = beanFactory.getBeanOrCreate(parameterType);
        }
        return args;
    }
}
