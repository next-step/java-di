package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.GenericBeanDefinition;
import com.interface21.context.annotation.Bean;
import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultListableBeanFactory implements BeanFactory {

  private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

  private final String[] basePackages;

  private final Map<Class<?>, BeanDefinition> beanDefinitionMap = new HashMap<>();

  private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

  public DefaultListableBeanFactory(String... basePackages) {
    this.basePackages = basePackages;
  }

  @Override
  public Set<Class<?>> getBeanClasses() {
    return beanDefinitionMap.keySet();
  }

  @Override
  public <T> T getBean(final Class<T> clazz) {
    if (clazz == null) {
      throw new IllegalArgumentException("Class must not be null");
    }

    Object object = singletonObjects.get(clazz);
    if(ObjectUtils.isEmpty(object)) {
      throw new NoSuchBeanDefinitionException("No bean of type " + clazz.getName() + " found.");
    }

    if(clazz.isAssignableFrom(object.getClass())) {
      return clazz.cast(object);
    }

    throw new NoSuchBeanDefinitionException("No bean of type " + clazz.getName() + " found.");
  }

  public void initialize() {
    Set<Class<?>> beans = new BeanScanner(basePackages).scan();
    for (Class<?> clazz : beans) {
      final GenericBeanDefinition beanDefinition = GenericBeanDefinition.from(clazz);
      beanDefinitionMap.put(clazz, beanDefinition);
    }

    for(Map.Entry<Class<?>, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
      initializeSingletonBean(entry.getKey());
    }
  }

  private void initializeSingletonBean(Class<?> beanClass) {
    Optional<Class<?>> concreteClass = BeanFactoryUtils.findConcreteClass(beanClass, getBeanClasses());
    if (concreteClass.isEmpty()) {
      throw new BeanInstantiationException(beanClass, "Could not find concrete class for bean class");
    }

    final BeanDefinition beanDefinition = beanDefinitionMap.get(concreteClass.get());
    final Class<?>[] parameterTypes = beanDefinition.getConstructor().getParameterTypes();
    final Object[] arguments = findArguments(parameterTypes);

    final Object instance = BeanUtils.instantiateClass(beanDefinition.getConstructor(), arguments);

    singletonObjects.put(concreteClass.get(), instance);

    log.info("Registered bean of type [{}] with name [{}] in the singleton context.",
        beanClass.getName(), instance.getClass().getName());
  }

  private Object[] findArguments(Class<?>[] parameterTypes) {
    Object[] arguments = new Object[parameterTypes.length];

    for(int i = 0; i < parameterTypes.length; i++) {
      Optional<Class<?>> implClass = BeanFactoryUtils.findConcreteClass(parameterTypes[i], getBeanClasses());
      if(implClass.isEmpty()) {
        throw new NoSuchBeanDefinitionException("Could not find concrete class for bean class");
      }
      arguments[i] = this.getBean(implClass.get());
    }

    return arguments;
  }

  @Override
  public void clear() {
    beanDefinitionMap.clear();
    singletonObjects.clear();
  }
}
