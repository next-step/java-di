package com.interface21.beans.factory.support;

import com.interface21.beans.BeanCircularException;
import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.context.stereotype.Controller;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DefaultListableBeanFactory implements BeanFactory {

  private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);
  private final Map<Class<?>, Object> singletonObjects = new HashMap<>();
  private final Set<Class<?>> beansCircularTracker = new HashSet<>();
  private final String[] basePackages;

  public DefaultListableBeanFactory(String... basePackages) {
    this.basePackages = basePackages;
  }

  @Override
  public Set<Class<?>> getBeanClasses() {
    return singletonObjects.keySet();
  }

  @Override
  public <T> T getBean(final Class<T> clazz) {
    Object bean = singletonObjects.get(clazz);
    return clazz.cast(bean);
  }

  @Override
  public void initialize() {

    List<BeanScanner> scanners = Arrays.asList(
        new ComponentAnnotationBeanScanner(basePackages),
        new ConfigurationBeanScanner(basePackages)
    );

    Set<Class<?>> beanClasses = scanners.stream()
        .flatMap(scanner -> scanner.scanForBeans().stream())
        .collect(Collectors.toSet());

    for (Class<?> beanClass : beanClasses) {
      var bean = createBean(beanClass, beanClasses);
      singletonObjects.put(beanClass, bean);
    }
  }

  private Object createBean(Class<?> beanClass, Set<Class<?>> beanClasses) {

    if (!beansCircularTracker.add(beanClass)) {
      throw new BeanCircularException(beanClass.getName());
    }

    Constructor<?> constructor = findAutoWiredConstructor(beanClass, beanClasses);
    Object[] params = resolveConstructorArguments(constructor, beanClasses);

    try {
      return constructor.newInstance(params);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      log.info(e.getMessage());
      throw new BeanInstantiationException(constructor, "bean생성을 실패했습니다 : +", e.getCause());
    }
  }

  /*@Autowired  붙은 생성자를 찾고, 없으면 기본 생성자를 반환한다. */

  private Constructor<?> findAutoWiredConstructor(Class<?> clazz, Set<Class<?>> beanClasses) {
    Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, beanClasses)
        .orElseThrow(() -> new IllegalArgumentException("클래스를 찾을 수 없습니다." + clazz.getName()));

    Constructor<?> autowiredConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);
    if (autowiredConstructor == null) {
      return concreteClass.getDeclaredConstructors()[0];
    }
    return autowiredConstructor;
  }

  private Object[] resolveConstructorArguments(Constructor<?> constructor,
      Set<Class<?>> beanClasses) {

    // singletonObjects 에 등록이 안되어 있으면 하위 빈 등록 재귀 호출
    return Arrays.stream(constructor.getParameterTypes())
        .map(paramType -> singletonObjects.computeIfAbsent(paramType,
            type -> createBean(type, beanClasses))).toArray();
  }

  public Map<Class<?>, Object> getControllers() {
    Map<Class<?>, Object> controllers = new HashMap();
    for (Class<?> clazz : singletonObjects.keySet()) {
      if (clazz.isAnnotationPresent(Controller.class)) {
        controllers.put(clazz, getBean(clazz));
      }
    }
    return controllers;
  }

  @Override
  public void clear() {
    singletonObjects.clear();
  }
}