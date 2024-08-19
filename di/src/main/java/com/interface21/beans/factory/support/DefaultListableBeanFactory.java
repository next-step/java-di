package com.interface21.beans.factory.support;

import com.interface21.beans.BeansCache;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.exception.NoSuchBeanDefinitionException;
import com.interface21.context.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private final BeansCache singletonObjects = new BeansCache();

    @Override
    public void initialize() {
        beanDefinitionMap.values().stream()
                         .map(BeanDefinition::getType)
                         .forEach(this::getBean);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.getAllBeanClasses();
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
        if (beanDefinitionMap.containsKey(requiredType.getName())) {
            log.info("빈 생성: {}", requiredType);
            object = beanDefinitionMap.get(requiredType.getName()).instantiateClass(this);
        } else {
            final Class<?> concreteClass = findConcreteClass(requiredType, getBeanDefinitionTypes());
            object = getBean(concreteClass);
        }

        singletonObjects.store(requiredType, object);
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
    public BeansCache getControllers() {
        return singletonObjects.filter(clazz -> clazz.isAnnotationPresent(Controller.class));
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(clazz.getName(), beanDefinition);
    }

    private Set<Class<?>> getBeanDefinitionTypes() {
        return beanDefinitionMap.values().stream()
                                .map(BeanDefinition::getType)
                                .collect(Collectors.toSet());
    }
}
