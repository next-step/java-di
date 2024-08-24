package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.GenericBeanDefinition;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;

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
    Arrays.stream(maps).forEach(beanDefinitionMap::putAll);
  }

  public void clear() {
    beanDefinitionMap.clear();
  }
}
