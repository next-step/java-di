package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.exception.NoSuchBeanDefinitionException;
import com.interface21.beans.factory.support.beancreator.ConfigurationClassBeanInstantiation;
import com.interface21.beans.factory.support.beancreator.ScannedBeanInstantiation;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.support.BeanScanner;
import com.interface21.context.support.ConfigurationInstanceAndMethod;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final Map<Class<?>, BeanInstantiation> beanInstantiationsMap = new HashMap<>();

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();
    private final Set<Class<?>> classes;
    private final BeanScanner beanScanner;

    public DefaultListableBeanFactory(final BeanScanner beanScanner) {
        this.classes = new HashSet<>();
        this.beanScanner = beanScanner;
    }

    public void initialize() {
        registerBeanInstantiationsAndClasses();

        loadAllBeans();
    }

    private void registerBeanInstantiationsAndClasses() {
        final Set<Class<?>> beanClasses = beanScanner.scanBeanClasses();
        beanClasses.forEach(clazz -> beanInstantiationsMap.put(clazz, new ScannedBeanInstantiation(clazz)));
        classes.addAll(beanClasses);

        final List<ConfigurationInstanceAndMethod> configurationBeans = beanScanner.scanConfigurationBeans();
        final Collection<Class<?>> configurationBeanTypes = beanScanner.scanConfigurationBeanTypes();
        configurationBeans.forEach(method ->
                beanInstantiationsMap.put(
                        method.method().getReturnType(),
                        new ConfigurationClassBeanInstantiation(method.object(), method.method())
                )
        );
        classes.addAll(configurationBeanTypes);
    }

    private void loadAllBeans() {
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

    private Object instantiateClass(final Class<?> requiredType) {
        Object object;
        if (beanInstantiationsMap.containsKey(requiredType)) {
            object = beanInstantiationsMap.get(requiredType).instantiateClass(this);
        } else {
            final Class<?> concreteClass = findConcreteClass(requiredType, classes);
            object = getBean(concreteClass);
        }

        singletonObjects.put(requiredType, object);
        return object;
    }

    private static Class<?> findConcreteClass(final Class<?> clazz, final Set<Class<?>> candidates) {
        return BeanFactoryUtils
                .findConcreteClass(clazz, candidates)
                .orElseThrow(() -> new NoSuchBeanDefinitionException(clazz));
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
