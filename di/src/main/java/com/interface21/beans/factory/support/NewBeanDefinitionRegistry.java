package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;

import java.util.Map;
import java.util.Set;

public interface NewBeanDefinitionRegistry {
    void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition);
}
