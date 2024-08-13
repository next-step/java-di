package com.interface21.context;

import com.interface21.beans.BeansCache;

import java.util.Set;

public interface ApplicationContext {
    void initialize();

    <T> T getBean(Class<T> clazz);

    Set<Class<?>> getBeanClasses();

    BeansCache getControllers();
}
