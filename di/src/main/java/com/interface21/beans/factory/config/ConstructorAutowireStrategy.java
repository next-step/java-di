package com.interface21.beans.factory.config;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;

import java.lang.reflect.Constructor;

public class ConstructorAutowireStrategy implements AutowireStrategy {

    @Override
    public Object autowire(BeanDefinition beanDefinition, BeanFactory beanFactory) {
        return BeanUtils.instantiateClass(
                (Constructor<?>) beanDefinition.getExecutable(),
                resolveDependencies(beanDefinition, beanFactory)
        );
    }
}
