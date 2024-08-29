package com.interface21.beans.factory.support;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import com.interface21.context.stereotype.Component;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public abstract class BeanScanner {
  protected static final String STEREOTYPE_PACKAGE = "com.interface21.context.stereotype";
  protected final String[] basePackages;

  protected BeanScanner(String... basePackages) {
    this.basePackages = basePackages;
  }

  protected Reflections createReflections() {
    return new Reflections(STEREOTYPE_PACKAGE, basePackages);
  }
}