package com.interface21.beans.factory.config;

import com.interface21.beans.BeanInstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ConfigurationClassBeanDefinition implements BeanDefinition {

    private final Class<?> type;
    private final String beanClassName;
    private final Class<?> factoryBeanType;
    private final Method factoryMethod;

    public ConfigurationClassBeanDefinition(final Method method, final Class<?> factoryBeanType) {
        final Class<?> beanType = method.getReturnType();
        this.type = beanType;
        this.beanClassName = beanType.getName();
        this.factoryBeanType = factoryBeanType;
        this.factoryMethod = method;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public String getBeanClassName() {
        return beanClassName;
    }

    @Override
    public Object createInstance(final BeanGetter beanGetter) {
        return createByBeanMethod(beanGetter);
    }

    private Object createByBeanMethod(final BeanGetter beanGetter) {
        try {
            final Object[] parameters = Arrays.stream(factoryMethod.getParameterTypes())
                    .map(beanGetter::getBean)
                    .toArray();

            factoryMethod.setAccessible(true);

            return factoryMethod.invoke(beanGetter.getBean(factoryBeanType), parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException(type, "Failed to instantiate bean", e);
        } finally {
            factoryMethod.setAccessible(false);
        }
    }

    public Class<?> getFactoryBeanType() {
        return this.factoryBeanType;
    }

    public Method getFactoryMethod() {
        return this.factoryMethod;
    }
}
