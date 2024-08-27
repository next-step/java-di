package com.interface21.beans.factory.config;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.beans.factory.support.ConfigurationBeanDefinition;

import java.lang.reflect.Method;

public class FactoryMethodAutowireStrategy implements AutowireStrategy {

    @Override
    public Object autowire(BeanDefinition beanDefinition, BeanFactory beanFactory) {

        if (beanDefinition instanceof ConfigurationBeanDefinition bd) {
            Object caller = beanFactory.getBean(bd.getExecutable().getDeclaringClass());

            return BeanFactoryUtils.invokeMethod((Method) bd.getExecutable(), caller, resolveDependencies(bd, beanFactory))
                    .orElseThrow(() -> new BeanInstantiationException(beanDefinition.getClass(), "factory method invocation failed"));
        }

        throw new IllegalArgumentException("Unsupported bean definition type: " + beanDefinition.getClass());
    }
}
