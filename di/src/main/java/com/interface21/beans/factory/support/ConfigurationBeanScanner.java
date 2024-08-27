package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ConfigurationBeanScanner {

  private final BeanDefinitionRegistry beanDefinitionRegistry;

  public ConfigurationBeanScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
    this.beanDefinitionRegistry = beanDefinitionRegistry;
  }

  public BeanDefinitionRegistry scan(List<String> basePackages) {
    Map<Class<?>, BeanDefinition> configurationBeanDefinitionMap = getBeanMethodReturnTypes(basePackages);
    return beanDefinitionRegistry.registerAll(configurationBeanDefinitionMap);
  }

  private Map<Class<?>, BeanDefinition> getBeanMethodReturnTypes(final List<String> basePackages) {
    final Set<Class<?>> classes = getConfigurationClasses(basePackages);

    Map<Class<?>, BeanDefinition> map = new HashMap<>();
    for (Class<?> clazz : classes) {
      BeanFactoryUtils.getBeanMethods(clazz, Bean.class).forEach(method -> {
        final Class<?> returnType = getReturnType(clazz, method);
        final BeanDefinition beanDefinition = new ConfigurationBeanDefinition(returnType, method);
        map.put(returnType, beanDefinition);
      });
    }

    return map;
  }

  private Set<Class<?>> getConfigurationClasses(final List<String> basePackages) {
    Reflections reflections = new Reflections(basePackages);

    return reflections.getTypesAnnotatedWith(Configuration.class);
  }

  private Class<?> getReturnType(final Class<?> clazz, final Method method) {
    Class<?> returnType = method.getReturnType();

    if (returnType.equals(void.class)) {
      throw new BeanCreationException(clazz, method);
    }

    return returnType;
  }
}
