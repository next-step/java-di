package com.interface21.beans.factory.support;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ConfigurationBeanScanner extends BeanScanner {

  public ConfigurationBeanScanner(String... basePackages) {
    super(basePackages);
  }

  public Set<Class<?>> getConfigurationClassesWithBean() {
    Reflections reflections = createReflections();

    return reflections.getTypesAnnotatedWith(Configuration.class).stream()
        .filter(Predicate.not(Class::isAnnotation))
        .filter(this::hasBeanAnnotatedMethod)
        .collect(Collectors.toSet());
  }

  private boolean hasBeanAnnotatedMethod(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredMethods())
        .anyMatch(method -> method.isAnnotationPresent(Bean.class));
  }
}
