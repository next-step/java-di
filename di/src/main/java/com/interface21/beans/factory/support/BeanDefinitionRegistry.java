package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;

import java.util.List;
import java.util.Set;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(Class<?> clazz);

    void removeBeanDefinition(Class<?> clazz);

    boolean containsBeanDefinition(Class<?> clazz);

    List<BeanDefinition> getBeanDefinitions();

    Set<Class<?>> getBeanClasses();
}
