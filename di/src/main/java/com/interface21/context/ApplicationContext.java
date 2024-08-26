package com.interface21.context;

import com.interface21.beans.factory.BeanFactory;
import java.util.Set;

public interface ApplicationContext {
    <T> T getBean(Class<T> clazz);

    Set<Class<?>> getBeanClasses();

    BeanFactory getBeanFactory();
}
