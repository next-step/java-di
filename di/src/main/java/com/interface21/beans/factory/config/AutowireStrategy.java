package com.interface21.beans.factory.config;

import com.interface21.beans.factory.BeanFactory;

import java.util.Arrays;

public interface AutowireStrategy {

    Object autowire(BeanDefinition beanDefinition, BeanFactory beanFactory);

    default Object[] resolveDependencies(BeanDefinition beanDefinition, BeanFactory beanFactory) {
        return Arrays.stream(beanDefinition.getParameterTypes())
                .map(beanFactory::getBean)
                .toArray(Object[]::new);
    }
}
