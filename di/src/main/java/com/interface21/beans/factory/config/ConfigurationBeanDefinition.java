package com.interface21.beans.factory.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.stream.Stream;

public class ConfigurationBeanDefinition implements BeanDefinition {
    private final Class<?> beanClass;
    private final Method beanMethod;

    private ConfigurationBeanDefinition(final Class<?> beanClass, final Method beanMethod) {
        this.beanClass = beanClass;
        this.beanMethod = beanMethod;
    }

    public static ConfigurationBeanDefinition from(final Method beanMethod) {
        return new ConfigurationBeanDefinition(beanMethod.getReturnType(), beanMethod);
    }

    @Override
    public Class<?> getType() {
        return beanClass;
    }

    @Override
    public String getBeanClassName() {
        return beanMethod.getName();
    }

    @Override
    public Object createBean(final Function<Class<?>, Object> beanSupplier) throws InvocationTargetException, IllegalAccessException {
        return beanMethod.invoke(beanSupplier.apply(beanMethod.getDeclaringClass()), createParameterArgs(beanSupplier));
    }

    private Object[] createParameterArgs(final Function<Class<?>, Object> beanSupplier) {
        return Stream.of(beanMethod.getParameterTypes())
                .map(beanSupplier)
                .toArray();
    }

    public Method getBeanMethod() {
        return beanMethod;
    }
}
