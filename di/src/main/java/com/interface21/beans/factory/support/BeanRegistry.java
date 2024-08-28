package com.interface21.beans.factory.support;

import java.lang.annotation.Annotation;

public interface BeanRegistry {

    boolean registeredBean(Object bean);

    Object getBean(Class<?> clazz);

    Object[] getBeanWithAnnotation(Class<? extends Annotation> annotationType);
}
