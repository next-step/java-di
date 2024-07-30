package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;

import java.util.List;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(Class<?> clazz);

    List<BeanDefinition> getBeanDefinitions();
}
