package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SimpleBeanDefinitionRegistry implements BeanDefinitionRegistry {

  private final Map<Class<?>, BeanDefinition> beanDefinitionMap = new HashMap<>();

  @Override
  public void register(Class<?> clazz, BeanDefinition beanDefinition) {
    beanDefinitionMap.put(clazz, beanDefinition);
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

  @SafeVarargs
  public final void registerAll(Map<Class<?>, BeanDefinition>... maps) {
    Arrays.stream(maps)
        .flatMap(map -> map.entrySet().stream())
        .forEach(this::registerBeanDefinitionMap);
  }

  private void registerBeanDefinitionMap(Entry<Class<?>, BeanDefinition> entry) {
    final Class<?> beanClass = entry.getKey();
    final BeanDefinition beanDefinition = entry.getValue();

    validateExistBeanName(beanDefinition);
    beanDefinitionMap.put(beanClass, beanDefinition);
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
