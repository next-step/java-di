package com.interface21.beans.factory.support;

public interface BeanRegistry {

    void registerBean(Object bean);

    boolean registeredBean(Object bean);

    boolean registeredBean(Class<?> clazz);

    Object getBean(Class<?> clazz);
}
