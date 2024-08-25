package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;

public interface BeanInitializer {

    boolean support(BeanDefinition beanDefinition);

    Object initializeBean(BeanDefinition beanDefinition);

    Object[] resolveArguments(BeanDefinition beanDefinition);
}
