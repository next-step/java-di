package com.interface21.beans.factory.config;

import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.beans.factory.support.BeanInstantiationException;
import com.interface21.beans.factory.support.NoUniqueBeanDefinitionException;
import com.interface21.core.util.StringUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class GenericBeanDefinition implements BeanDefinition{

  private final Class<?> type;
  private final String beanClassName;
  private final Constructor<?> constructor;

  private GenericBeanDefinition(Class<?> type, String beanNamae, Constructor<?> constructor) {
    this.type = type;
    this.beanClassName = beanNamae;
    this.constructor = constructor;
  }

  public static GenericBeanDefinition from(Class<?> beanClass) {
    final String beanName = StringUtils.makeBeanName(beanClass.getSimpleName());

    return new GenericBeanDefinition(beanClass, beanName, getConstructor(beanClass));
  }

  @Override
  public Class<?> getType() {
    return type;
  }

  @Override
  public String getBeanClassName() {
    return beanClassName;
  }

  @Override
  public Object createBean(final Function<Class<?>, Object> beanSupplier)
      throws InvocationTargetException, IllegalAccessException, InstantiationException {
    return constructor.newInstance(createParameterArgs(beanSupplier));
  }

  private Object[] createParameterArgs(final Function<Class<?>, Object> beanSupplier) {
    return Stream.of(constructor.getParameterTypes())
        .map(beanSupplier)
        .toArray();
  }

  public static Constructor<?> getConstructor(Class<?> clazz) {
    return getAutowiredConstructor(clazz)
        .orElseGet(() -> getDefaultConstructor(clazz));
  }

  public Constructor<?> getConstructor() {
    return this.constructor;
  }

  private static Optional<Constructor> getAutowiredConstructor(Class<?> clazz) {
    Set<Constructor> injectedConstructors = BeanFactoryUtils.getInjectedConstructors(clazz);

    if (injectedConstructors.size() > 1) {
      throw new NoUniqueBeanDefinitionException("No qualifying bean of type " + clazz.getTypeName());
    }

    return injectedConstructors.stream().findAny();
  }

  private static Constructor<?> getDefaultConstructor(Class<?> clazz) {
    Constructor<?>[] constructors = clazz.getConstructors();

    if (constructors.length == 1) {
      return constructors[0];
    } else if (constructors.length == 0) {
      return null;
    }

    throw new BeanInstantiationException(clazz, "No default constructor found.");
  }
}
