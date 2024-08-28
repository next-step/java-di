package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SimpleBeanDefinitionRegistry implements BeanDefinitionRegistry {

  private final Map<Class<?>, BeanDefinition> beanDefinitionMap = new HashMap<>();

  @Override
  public void register(Class<?> clazz, BeanDefinition beanDefinition) {
    registerBeanDefinitionMap(clazz, beanDefinition);
  }

  @Override
  public Set<Class<?>> getBeanClasses() {
    return beanDefinitionMap.keySet();
  }

  @Override
  public BeanDefinition get(Class<?> clazz) {
    return beanDefinitionMap.values().stream()
        .filter(beanDefinition -> beanDefinition.getType().equals(clazz))
        .findFirst()
        .orElseGet(() -> findByConcreteClass(clazz));
  }

  private BeanDefinition findByConcreteClass(final Class<?> clazz) {
    final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses())
        .orElseThrow(() -> new BeanInstantiationException(clazz, "Could not autowire. No concrete class found for %s.".formatted(clazz.getName())));

    return beanDefinitionMap.values().stream()
        .filter(beanDefinition -> beanDefinition.getType().equals(concreteClass))
        .findFirst()
        .orElseThrow(() -> new BeanInstantiationException(clazz, "cannot find bean for " + clazz.getName()));
  }

  @Override
  public Map<Class<?>, BeanDefinition> getBeanDefinitions() {
    return beanDefinitionMap;
  }

  @Override
  public BeanDefinitionRegistry registerAll(Map<Class<?>, BeanDefinition> beanDefinitions) {
    for(Entry<Class<?>, BeanDefinition> entry : beanDefinitions.entrySet()) {
      this.registerBeanDefinitionMap(entry.getKey(), entry.getValue());
    }

    return this;
  }

  private void registerBeanDefinitionMap(Class<?> clazz, BeanDefinition beanDefinition) {
    validateExistBeanName(beanDefinition);
    beanDefinitionMap.put(clazz, beanDefinition);
  }

  private void validateExistBeanName(final BeanDefinition newBeanDefinition) {
    final String newBeanClassName = newBeanDefinition.getBeanClassName();
    if (beanDefinitionMap.values().stream().anyMatch(exist -> exist.isSameBeanClassName(newBeanClassName))) {
      throw new BeanCreationException(newBeanClassName);
    }
  }

  public void clear() {
    beanDefinitionMap.clear();
  }
}
