package com.interface21.beans.factory;

import com.interface21.beans.factory.support.BeanDefinitionRegistry;

import java.util.Set;

public interface BeanFactory extends BeanDefinitionRegistry {

    Set<Class<?>> getBeanClasses();

    <T> T getBean(Class<T> clazz);

    void initialize();

    void clear();
}
