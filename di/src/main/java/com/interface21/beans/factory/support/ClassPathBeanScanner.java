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

public class ClassPathBeanScanner {

  private final BeanDefinitionRegistry beanDefinitionRegistry;

  private static final List<Class<? extends Annotation>> beanAnnotations =
      List.of(Controller.class, Service.class, Repository.class, Configuration.class);

  public ClassPathBeanScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
    this.beanDefinitionRegistry = beanDefinitionRegistry;
  }

  public BeanDefinitionRegistry scan(List<String> basePackages) {
    Reflections reflections = new Reflections(basePackages);

    Map<Class<?>, BeanDefinition> beanDefinitionMap = beanAnnotations.stream()
        .map(reflections::getTypesAnnotatedWith)
        .flatMap(Set::stream)
        .collect(Collectors.toMap(clazz -> clazz, GenericBeanDefinition::from));

    return beanDefinitionRegistry.registerAll(beanDefinitionMap);
  }
}
