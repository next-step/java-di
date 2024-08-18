package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.stereotype.Component;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();
    private final String[] basePackages;

    public DefaultListableBeanFactory(String... basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.keySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        return (T) singletonObjects.get(clazz);
    }

    public void initialize() {
        Reflections reflections = new Reflections((Object[]) basePackages);
        Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);

        for (Class<?> componentClass : components) {
            var bean = createBean(componentClass);
            singletonObjects.put(componentClass, bean);
        }
    }

    private Object createBean(Class<?> beanClass) {
        Constructor<?> constructor = findAutowiredConstructor(beanClass);
        Object[] params = resolveConstructorArguments(constructor);

        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.info(e.getMessage());
            throw new BeanInstantiationException(constructor, "bean생성을 실패했습니다 : +",e.getCause());
        }
    }

    /*@Autowired  붙은 생성자를 찾고, 없으면 기본 생성자를 반환한다. */
    private Constructor<?> findAutowiredConstructor(Class<?> beanClass) {
        return Arrays.stream(beanClass.getDeclaredConstructors())
            .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
            .findFirst()
            .orElse(beanClass.getDeclaredConstructors()[0]);
    }

    private Object[] resolveConstructorArguments(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
            .map(paramType -> {
                if (singletonObjects.containsKey(paramType)) {
                    return singletonObjects.get(paramType);
                } else {
                    Object bean = createBean(paramType);
                    singletonObjects.put(paramType, bean);
                    return bean;
                }
            })
            .toArray();
    }

    @Override
    public void clear() {
        singletonObjects.clear();
        beanDefinitionMap.clear();
    }
}