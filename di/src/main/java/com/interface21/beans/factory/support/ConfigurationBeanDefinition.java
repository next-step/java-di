package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public class ConfigurationBeanDefinition implements BeanDefinition {

    private final Class<?> beanClass;
    private final Constructor<?> constructor;
    
    public ConfigurationBeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        ConstructorHolder constructorHolder = ConstructorResolver.resolve(beanClass);
        this.constructor = constructorHolder.constructor();
    }

    @Override
    public Class<?> getType() {
        return beanClass;
    }

    @Override
    public String getBeanClassName() {
        return beanClass.getSimpleName();
    }

    @Override
    public Constructor<?> getConstructor() {
        return constructor;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return new Class[0];
    }

    // TODO: 이상하다
    @Override
    public Object[] resolveArguments(BeanFactory beanFactory) {
        return new Object[0];
    }
}
