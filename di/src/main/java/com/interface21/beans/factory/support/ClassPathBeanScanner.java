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

public final class ClassPathBeanScanner {

  private static final List<Class<? extends Annotation>> beanAnnotations =
      List.of(Controller.class, Service.class, Repository.class, Configuration.class);

  private ClassPathBeanScanner() {
  }

  public static Map<Class<?>, BeanDefinition> scan(List<String> basePackages) {
    Reflections reflections = new Reflections(basePackages);

    return beanAnnotations.stream()
        .map(reflections::getTypesAnnotatedWith)
        .flatMap(Set::stream)
        .collect(Collectors.toMap(clazz -> clazz, GenericBeanDefinition::from));
  }
}
