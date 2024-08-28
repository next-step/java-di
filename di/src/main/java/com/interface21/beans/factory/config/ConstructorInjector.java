package com.interface21.beans.factory.config;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;

import java.lang.reflect.Constructor;

public class ConstructorInjector implements Injector {

    @Override
    public Object inject(BeanDefinition beanDefinition, BeanFactory beanFactory) {
        return BeanUtils.instantiateClass(
                (Constructor<?>) beanDefinition.getExecutable(),
                resolveDependencies(beanDefinition, beanFactory)
        );
    }
}
