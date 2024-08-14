package com.interface21.beans.factory;

import com.interface21.beans.BeansCache;

import java.util.Set;

public interface BeanFactory {

    Set<Class<?>> getBeanClasses();

    <T> T getBean(Class<T> clazz);

    void clear();

    BeansCache getControllers();
}
