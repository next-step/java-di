package com.interface21.beans.factory;

import com.interface21.beans.BeansCache;
import com.interface21.beans.factory.support.BeanInstantiation;

import java.util.Set;

public interface BeanFactory {

    void initialize();

    Set<Class<?>> getBeanClasses();

    <T> T getBean(Class<T> clazz);

    void clear();

    BeansCache getControllers();

    void registerBeanInstantiation(Class<?> clazz, BeanInstantiation beanInstantiation);
}
