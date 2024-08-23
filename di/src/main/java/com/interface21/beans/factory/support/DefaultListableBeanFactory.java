package com.interface21.beans.factory.support;

import com.interface21.beans.BeanCircularException;
import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.context.stereotype.Component;
import com.interface21.context.stereotype.Controller;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DefaultListableBeanFactory implements BeanFactory {

  private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);
  private static final String STEREOTYPE_PACKAGE = "com.interface21.context.stereotype";

  private final Map<Class<?>, Object> singletonObjects = new HashMap<>();
  private final String[] basePackages;
  private final Set<Class<?>> beansCircularTracker = new HashSet<>();

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

  public void initialize() {
    Reflections reflections = new Reflections(STEREOTYPE_PACKAGE,
        basePackages);
    Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class).stream()
        .filter(Predicate.not(Class::isAnnotation))
        .collect(Collectors.toSet());

    for (Class<?> componentClass : components) {
      var bean = createBean(componentClass, components);
      singletonObjects.put(componentClass, bean);
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
    } finally {
      beansCircularTracker.remove(beanClass);
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