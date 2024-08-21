package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import java.util.Set;

public interface BeanDefinitionRegistry {

    void register(Class<?> clazz, BeanDefinition beanDefinition);

    Set<Class<?>> getBeanClasses();

    BeanDefinition get(Class<?> clazz);
}
