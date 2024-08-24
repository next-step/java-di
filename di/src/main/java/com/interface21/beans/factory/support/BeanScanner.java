package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.GenericBeanDefinition;
import com.interface21.context.annotation.Configuration;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public final class BeanScanner {

  private final Reflections reflections;
  private final List<Class<? extends Annotation>> beanAnnotations;

  public BeanScanner(List<String> basePackages) {
    this.reflections = new Reflections(basePackages);
    this.beanAnnotations = List.of(Controller.class, Service.class, Repository.class, Configuration.class);
  }

  public Map<Class<?>, BeanDefinition> scan() {
    return beanAnnotations.stream()
        .map(reflections::getTypesAnnotatedWith)
        .flatMap(Set::stream)
        .collect(Collectors.toMap(clazz -> clazz, GenericBeanDefinition::from));
  }
}
