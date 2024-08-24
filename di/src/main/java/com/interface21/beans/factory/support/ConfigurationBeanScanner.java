package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.reflections.Reflections;

public final class ConfigurationBeanScanner {

  private final Set<Class<?>> configurationClasses;
  private final List<String> basePackages;

  public ConfigurationBeanScanner(Class<?>... configurationClasses) {
    this.configurationClasses = Arrays.stream(configurationClasses).collect(Collectors.toSet());
    this.basePackages = this.findBasePackages();
  }

  private List<String> findBasePackages() {
    return this.configurationClasses.stream()
        .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
        .map(clazz -> clazz.getAnnotation(ComponentScan.class).basePackages())
        .flatMap(Stream::of)
        .toList();
  }

  public Map<Class<?>, BeanDefinition> scan() {
    return this.getBeanMethodReturnTypes(basePackages);
  }

  private Map<Class<?>, BeanDefinition> getBeanMethodReturnTypes(final List<String> basePackages) {
    final Set<Class<?>> classes = this.getConfigurationClasses(basePackages);

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

  public List<String> getBasePackages() {
    return basePackages;
  }
}
