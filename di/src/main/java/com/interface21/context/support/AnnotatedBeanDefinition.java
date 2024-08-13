package com.interface21.context.support;

import com.interface21.beans.factory.support.GenericBeanDefinition;
import com.interface21.beans.factory.support.InjectMode;
import java.lang.reflect.Method;

public class AnnotatedBeanDefinition extends GenericBeanDefinition {

    private final Method method;

    public AnnotatedBeanDefinition(Class<?> bean, Method method) {
        super(bean);
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public InjectMode getInjectMode() {
        return InjectMode.CONFIGURATION;
    }
}
