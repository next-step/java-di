package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.exception.NoSuchBeanDefinitionException;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import com.interface21.core.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();
    private final Set<Class<?>> classes;

    public DefaultListableBeanFactory(String[] basePackages) {
        this.classes = ReflectionUtils.getTypesAnnotatedWith(
                basePackages,
                Repository.class, Service.class, Controller.class);
    }

    public void initialize() {
        classes.forEach(this::getBean);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.keySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        Object bean = singletonObjects.get(clazz);
        if (bean != null) {
            return (T) bean;
        }
        return (T) instantiateClass(clazz);
    }

    private Object instantiateClass(Class<?> requiredType) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(requiredType, classes)
                                                 .orElseThrow(()-> new NoSuchBeanDefinitionException(requiredType));
        Object object;
        if (concreteClass != requiredType) {
            object = getBean(concreteClass);
        } else {
            object = doInstantiateClass(requiredType);
        }

        singletonObjects.put(requiredType, object);
        return object;
    }

    private Object doInstantiateClass(Class<?> concreteClass) {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);

        if (constructor == null) {
            return BeanUtils.instantiate(concreteClass);
        }

        return BeanUtils.instantiateClass(
                constructor,
                getArgumentsFromSingletonObjects(
                        constructor.getParameters()
                )
        );
    }

    private Object[] getArgumentsFromSingletonObjects(Parameter[] parameters) {
        return Arrays.stream(parameters)
                     .map(parameter -> getBean(parameter.getType()))
                     .toArray();
    }

    @Override
    public void clear() {
    }

    @Override
    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> preInstantiateBeans = singletonObjects.keySet();

        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : preInstantiateBeans) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllers.put(clazz, getBean(clazz));
            }
        }
        return controllers;
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        // 아직 아무데서도 쓰지 않음
//        beanDefinitionMap.put(clazz.getName(), beanDefinition);
    }
}
