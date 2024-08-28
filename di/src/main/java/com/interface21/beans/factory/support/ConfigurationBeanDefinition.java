package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.Injector;
import com.interface21.beans.factory.config.BeanDefinition;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements BeanDefinition {

    private final InjectionType injectionType;
    private final Class<?> beanClass;
    private final Method beanMethod;

    public ConfigurationBeanDefinition(Class<?> beanClass, Method beanMethod) {
        injectionType = InjectionType.METHOD;
        this.beanClass = beanClass;
        this.beanMethod = beanMethod;
    }

    @Override
    public Class<?> getType() {
        return beanClass;
    }

    @Override
    public Executable getExecutable() {
        return beanMethod;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return beanMethod.getParameterTypes();
    }

    @Override
    public Injector getInjector() {
        return injectionType.getInjector();
    }
}
