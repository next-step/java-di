package com.interface21.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import com.interface21.beans.factory.BeanFactory;

public class ConstructorArgumentResolver {

    public static Object[] resolveConstructorArguments(
            Constructor<?> constructor, BeanFactory beanFactory) {

        if (constructor.getParameterCount() == 0) {
            return new Object[0];
        }

        return Arrays.stream(constructor.getParameters())
                .map(Parameter::getType)
                .map(beanFactory::getBeanOrCreate)
                .toArray();
    }
}
