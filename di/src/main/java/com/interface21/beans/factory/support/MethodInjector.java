package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import java.lang.reflect.Method;
import java.util.Set;

public class MethodInjector implements Injector {
    @Override
    public Object inject(BeanDefinition beanDefinition, BeanFactory beanFactory) {
        Object bean = BeanUtils.instantiate(beanDefinition.getType());
        Set<Method> methods = beanDefinition.getMethods();

        for (Method method : methods) {
            Object[] dependencies = resolveDependencies(beanFactory, method.getParameterTypes());
            BeanFactoryUtils.invokeMethod(method, bean, dependencies);
        }

        return bean;
    }
}
