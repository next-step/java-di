package com.interface21.beans.factory.support;

public interface BeanRegistry {

    boolean registeredBean(Object bean);

    Object getBean(Class<?> clazz);
}
