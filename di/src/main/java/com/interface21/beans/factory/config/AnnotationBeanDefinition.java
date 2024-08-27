package com.interface21.beans.factory.config;

import com.interface21.beans.factory.support.ConstructorHolder;
import com.interface21.beans.factory.support.ConstructorResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;

public class AnnotationBeanDefinition implements BeanDefinition {

    private final Class<?> beanType;
    private final boolean autowireMode;
    private final Constructor<?> constructor;
    private final Class<?>[] argumentTypes;

    public AnnotationBeanDefinition(Class<?> beanType) {
        this.beanType = beanType;
        ConstructorHolder constructorHolder = ConstructorResolver.resolve(beanType);
        this.constructor = constructorHolder.constructor();
        this.autowireMode = constructorHolder.autowiredMode();
        this.argumentTypes = constructor.getParameterTypes();
    }

    @Override
    public Class<?> getType() {
        return beanType;
    }


    public Executable getExecutable() {
        return constructor;
    }

    public Class<?>[] getParameterTypes() {
        return argumentTypes;
    }

    @Override
    public AutowireStrategy autowireStrategy() {
        return new ConstructorAutowireStrategy();
    }
}
