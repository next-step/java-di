package com.interface21.beans.factory.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public interface MethodBeanDefinition extends BeanDefinition {

    Method getMethod();

    default Constructor<?> getConstructor() {
        throw new UnsupportedOperationException("MethodBeanDefinition does not support getConstructor()");
    }
}
