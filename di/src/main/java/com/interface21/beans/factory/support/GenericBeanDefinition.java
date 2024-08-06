package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class GenericBeanDefinition implements BeanDefinition {

    private final Class<?> bean;
    private final Constructor<?> constructor;
    private final Set<Field> fields;
    private final Set<Method> methods;


    public GenericBeanDefinition(Class<?> bean) {
        this.bean = bean;
        this.constructor = BeanFactoryUtils.getInjectedConstructor(bean);
        this.fields = BeanFactoryUtils.getInjectedFields(bean);
        this.methods = BeanFactoryUtils.getInjectedMethods(bean);
    }

    @Override
    public Class<?> getType() {
        return bean;
    }

    @Override
    public String getBeanClassName() {
        return bean.getName();
    }

    @Override
    public InjectMode getInjectMode() {
        if (constructor != null) {
            return InjectMode.CONSTRUCTOR;
        }
        if (!methods.isEmpty()) {
            return InjectMode.METHOD;
        }
        if (!fields.isEmpty()) {
            return InjectMode.FIELD;
        }
        return InjectMode.NONE;
    }

    @Override
    public Constructor<?> getConstructor() {
        return constructor;
    }

    @Override
    public Set<Method> getMethods() {
        return methods;
    }

    @Override
    public Set<Field> getFields() {
        return fields;
    }
}
