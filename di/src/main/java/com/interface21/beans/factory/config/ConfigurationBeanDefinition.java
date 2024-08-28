package com.interface21.beans.factory.config;

import com.interface21.core.util.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class ConfigurationBeanDefinition implements BeanDefinition{

  private final Class<?> beanClass;
  private final String beanClassName;
  private final Method beanMethod;

  public ConfigurationBeanDefinition(final Class<?> beanClass, final Method beanMethod) {
    this.beanClass = beanClass;
    this.beanClassName = StringUtils.makeBeanName(beanClass.getSimpleName());
    this.beanMethod = beanMethod;
  }

  @Override
  public Class<?> getType() {
    return beanClass;
  }

  @Override
  public String getBeanClassName() {
    return beanClassName;
  }

  @Override
  public Object createBean(Function<Class<?>, Object> beanSupplier) throws InvocationTargetException, IllegalAccessException {
    Object[] args = createParameterArgs(beanSupplier);
    return beanMethod.invoke(beanSupplier.apply(beanMethod.getDeclaringClass()), args);
  }

  @Override
  public boolean isSameBeanClassName(final String BeanClassName) {
    return this.beanClassName.equals(BeanClassName);
  }

  private Object[] createParameterArgs(final Function<Class<?>, Object> beanSupplier) {
    Class<?>[] parameterTypes = beanMethod.getParameterTypes();
    Object[] args = new Object[parameterTypes.length];

    for (int i = 0; i < parameterTypes.length; i++) {
      args[i] = beanSupplier.apply(parameterTypes[i]);
    }

    return args;
  }
}
