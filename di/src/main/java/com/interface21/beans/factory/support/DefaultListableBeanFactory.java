package com.interface21.beans.factory.support;

import com.interface21.beans.BeanCurrentlyInCreationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListableBeanFactory implements BeanFactory {

  private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

  private final SimpleBeanDefinitionRegistry beanRegistry;

  private final Map<String, Object> singletonObjects = new HashMap<>();
  private final Set<BeanDefinition> earlySingletonObjects = new HashSet<>();

  public DefaultListableBeanFactory(final SimpleBeanDefinitionRegistry beanRegistry) {
    this.beanRegistry = beanRegistry;
  }

  @Override
  public Set<Class<?>> getBeanClasses() {
    return beanRegistry.getBeanClasses();
  }

  public Map<Class<?>, Object> getControllers() {
    Map<Class<?>, Object> controllers = new HashMap<>();
    beanRegistry.getBeanDefinitions()
        .forEach((clazz, beanDefinition) -> findController(clazz, beanDefinition.getBeanName(), controllers));

    return controllers;
  }

  private void findController(Class<?> clazz, String beanName, Map<Class<?>, Object> controllers) {
    if (clazz.isAnnotationPresent(Controller.class)) {
      controllers.put(clazz, singletonObjects.get(beanName));
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getBean(final Class<T> clazz) {
    return (T) singletonObjects.entrySet()
        .stream()
        .filter(entry -> clazz.isAssignableFrom(entry.getValue().getClass()))
        .findFirst()
        .map(Map.Entry::getValue)
        .orElseThrow(() -> new NoSuchBeanDefinitionException(clazz.getName()));
  }

  @SuppressWarnings("unchecked")
  public <T> T getBean(final String beanName) {
    return (T) singletonObjects.get(beanName);
  }

  public void initialize() {
    beanRegistry.getBeanDefinitions()
        .forEach((clazz, beanDefinition) -> {
          initBean(new AbstractMap.SimpleEntry<>(clazz, beanDefinition));
        });
  }

  private Object initBean(Map.Entry<Class<?>, BeanDefinition> beanDefinitionMap) {
    final BeanDefinition beanDefinition = beanDefinitionMap.getValue();

    validateBeanCurrentlyInCreation(beanDefinitionMap.getValue());

    final String beanClassName = beanDefinition.getBeanName();
    if (ObjectUtils.isNotEmpty(singletonObjects.get(beanClassName))) {
      return singletonObjects.get(beanClassName);
    }

    earlySingletonObjects.add(beanDefinition);

    final Object object = this.instantiateBean(beanDefinition);
    singletonObjects.put(beanClassName, object);
    earlySingletonObjects.remove(beanDefinition);

    log.info("Registered bean of type [{}] with name [{}] in the singleton context.", beanClassName, object.getClass().getName());

    return object;
  }

  private void validateBeanCurrentlyInCreation(BeanDefinition beanDefinition) {
    if(earlySingletonObjects.contains(beanDefinition)) {
      throw new BeanCurrentlyInCreationException(beanDefinition.getBeanName());
    }
  }

  private Object instantiateBean(final BeanDefinition beanDefinition) {
    try {
      return beanDefinition.createBean(this::getOrCreateBean);
    } catch (final InvocationTargetException | IllegalAccessException | InstantiationException e) {
      throw new BeanInstantiationException(beanDefinition.getType(), e.getMessage(), e);
    }
  }

  private Object getOrCreateBean(final Class<?> parameterType) {
    final BeanDefinition beanDefinition = beanRegistry.get(parameterType);
    final String beanClassName = beanDefinition.getBeanName();

    if (singletonObjects.containsKey(beanClassName)) {
      return singletonObjects.get(beanClassName);
    }

    return initBean(new AbstractMap.SimpleEntry<>(parameterType, beanDefinition));
  }

  @Override
  public void clear() {
    beanRegistry.clear();
    singletonObjects.clear();
  }
}
