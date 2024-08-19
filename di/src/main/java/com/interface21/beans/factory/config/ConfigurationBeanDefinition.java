package com.interface21.beans.factory.config;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements BeanDefinition {
    private final Method method;
    private final Class<?> configurationClass;

    public ConfigurationBeanDefinition(Method method) {
        this.method = method;
        this.configurationClass = method.getDeclaringClass();
    }

    @Override
    public Class<?> getType() {
        return method.getReturnType();
    }

    @Override
    public Object instantiateClass(final BeanFactory beanFactory) {
        try {
            Object targetObject = BeanUtils.instantiate(configurationClass);
            Object[] arguments = BeanDefinition.getArgumentsFromSingletonObjects(method.getParameters(), beanFactory);

            return method.invoke(targetObject, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
