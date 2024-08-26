package com.interface21.beans.factory.support;

import java.lang.reflect.Method;

public class BeanCreationException extends RuntimeException {

  public BeanCreationException(Class<?> clazz, Method method) {
    super(getMessage(clazz.getName(), method.getName()));
  }

  public BeanCreationException(String beanName) {
    super(String.format("Error creating bean with name '%s': Bean with name '%s' already exists.", beanName, beanName));
  }

  private static String getMessage(final String className, final String methodName) {
    return String.format("Bean method %s in class [%s] returned a void type, which is not allowed. "
        + "@Bean methods must return a value.", methodName, className);
  }
}
