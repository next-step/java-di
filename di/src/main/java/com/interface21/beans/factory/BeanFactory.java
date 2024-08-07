package com.interface21.beans.factory;

import java.util.Set;

public interface BeanFactory {

    Set<Class<?>> getBeanClasses();

    <T> T getBean(Class<T> clazz);

    <T> T getBean(String beanName);

    void clear();
}
