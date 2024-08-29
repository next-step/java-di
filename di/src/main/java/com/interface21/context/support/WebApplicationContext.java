package com.interface21.context.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;

import com.interface21.context.annotation.ComponentScan;
import java.util.Arrays;
import java.util.Set;

public class WebApplicationContext implements ApplicationContext {

  private final DefaultListableBeanFactory beanFactory;

  public WebApplicationContext(Class<?> configClass) {

    String[] basePackages = getBasePackages(configClass);
    beanFactory = new DefaultListableBeanFactory(basePackages);
    beanFactory.initialize();
  }

  private String[] getBasePackages(Class<?> configClass) {

    return Arrays.stream(configClass.getAnnotations())
        .filter(annotation -> annotation.annotationType().equals(ComponentScan.class))
        .map(annotation -> (ComponentScan) annotation)
        .map(ComponentScan::value)
        .flatMap(Arrays::stream)
        .toArray(String[]::new);
  }

  @Override
  public BeanFactory getBeanFactory() {
    return beanFactory;
  }

}
