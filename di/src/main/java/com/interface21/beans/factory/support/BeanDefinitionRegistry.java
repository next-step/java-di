package com.interface21.beans.factory.support;

import java.util.List;
import java.util.Set;

import com.interface21.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition);

    Set<Class<?>> getBeanClasses();

    List<BeanDefinition> getBeanDefinitions();

    int getBeanDefinitionCount();
}
