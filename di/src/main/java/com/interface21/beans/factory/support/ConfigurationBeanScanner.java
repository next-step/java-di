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

  private ConfigurationBeanScanner() {
  }

  public static Map<Class<?>, BeanDefinition> scan(List<String> basePackages) {
    return getBeanMethodReturnTypes(basePackages);
  }

  private static Map<Class<?>, BeanDefinition> getBeanMethodReturnTypes(final List<String> basePackages) {
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

  private static Set<Class<?>> getConfigurationClasses(final List<String> basePackages) {
    Reflections reflections = new Reflections(basePackages);

    return reflections.getTypesAnnotatedWith(Configuration.class);
  }

  private static Class<?> getReturnType(final Class<?> clazz, final Method method) {
    Class<?> returnType = method.getReturnType();

    if (returnType.equals(void.class)) {
      throw new BeanCreationException(clazz, method);
    }

    return returnType;
  }
}
