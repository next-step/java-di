package com.interface21.beans.factory.support;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import com.interface21.beans.factory.config.BeanDefinition;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final Map<Class<?>, Object> singletonObjects = new ConcurrentHashMap<>();

    public DefaultListableBeanFactory(String[] basePackages) {
        var beansClasses = BeanScanner.scanBeans(basePackages);
        beansClasses.stream()
                .map(AnnotationBeanDefinition::new)
                .forEach(
                        beanDefinition ->
                                beanDefinitionMap.put(
                                        beanDefinition.getBeanClassName(), beanDefinition));
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.values().stream()
                .map(BeanDefinition::getType)
                .collect(Collectors.toSet());
    }

    public void initialize() {
        beanDefinitionMap.forEach(
                (beanName, beanDefinition) -> {
                    Class<?> beanClazz = beanDefinition.getType();
                    log.info("Bean [{}] is being created", beanClazz.getName());
                    singletonObjects.put(beanClazz, getBeanOrCreate(beanClazz));
                });
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return (T) singletonObjects.get(clazz);
    }

    @Override
    public Object getBeanOrCreate(Class<?> clazz) {
        Class<?> cls =
                BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses())
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "No concrete class found for " + clazz.getName()));

        Object o = singletonObjects.get(cls);
        if (o != null) {
            return o;
        }
        Constructor<?> constructor = ConstructorResolver.resolveConstructor(cls);
        Object[] arg = ConstructorArgumentResolver.resolveConstructorArguments(constructor, this);
        return registerBean(constructor, arg);
    }

    private Object registerBean(Constructor<?> constructor, Object[] arg) {
        try {
            return addBean(constructor.newInstance(arg));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object addBean(Object bean) {
        singletonObjects.put(bean.getClass(), bean);
        return bean;
    }

    @Override
    public void clear() {}
}
