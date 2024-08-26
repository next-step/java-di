package com.interface21.beans.factory.support;

import com.interface21.context.annotation.ComponentScan;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ComponentScanBasePackageResolver {

  private ComponentScanBasePackageResolver() {
  }

  public static List<String> getBasePackages(Class<?>... classes) {
    final Set<Class<?>> configurationClasses = Arrays.stream(classes).collect(Collectors.toSet());

    return configurationClasses.stream()
        .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
        .map(clazz -> clazz.getAnnotation(ComponentScan.class).basePackages())
        .flatMap(Stream::of)
        .toList();
  }
}
