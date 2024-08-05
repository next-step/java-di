package com.interface21.beans.factory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public interface BeanFactory {

    String BEAN_FACTORY_CONTEXT_ATTRIBUTE = BeanFactory.class.getName();

    Set<Class<?>> getBeanClasses();

    <T> T getBean(Class<T> clazz);

    List<Object> getBeansForAnnotation(final Class<? extends Annotation> annotationType);

    void clear();
}
