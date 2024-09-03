package com.interface21.beans.factory.support;

import com.interface21.context.stereotype.Component;
import org.reflections.Reflections;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ComponentAnnotationBeanScanner extends BeanScanner {

  public ComponentAnnotationBeanScanner(String... basePackages) {
    super(basePackages);
  }

  @Override
  protected Set<Class<?>> scanForBeans() {
    Reflections reflections = createReflections();
    return reflections.getTypesAnnotatedWith(Component.class).stream()
        .filter(Predicate.not(Class::isAnnotation))
        .collect(Collectors.toSet());
  }
}
