package com.interface21.beans.factory.config;

import java.lang.reflect.Method;
import java.util.List;

public interface BeanDefinition {

    Class<?> getType();

    String getBeanClassName();

    BeanScope getScope();

    boolean isAssignableTo(Class<?> clazz);

    boolean isConfiguration();

    List<Method> getBeanCreateMethods();

    boolean isSubBeanDefinition();

    BeanDefinition getSuperBeanDefinition();
}
