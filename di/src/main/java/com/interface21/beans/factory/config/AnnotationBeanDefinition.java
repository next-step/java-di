package com.interface21.beans.factory.config;

import com.interface21.beans.factory.support.ConstructorHolder;
import com.interface21.beans.factory.support.ConstructorResolver;

import java.lang.reflect.Constructor;

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

    @Override
    public String getBeanClassName() {
        return beanType.getSimpleName();
    }

    public boolean isAutowireMode() {
        return autowireMode;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public Class<?>[] getParameterTypes() {
        return argumentTypes;
    }
}
