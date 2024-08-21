package com.interface21.beans.factory.config;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.support.BeanFactoryUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeanDefinitions {
    private final Map<Class<?>, BeanDefinition> beanDefinitionMap;

    public BeanDefinitions() {
        this(new HashSet<>());
    }

    public BeanDefinitions(final Set<Class<?>> beanClasses) {
        beanDefinitionMap = beanClasses.stream()
                .map(SimpleBeanDefinition::from)
                .collect(Collectors.toMap(
                        SimpleBeanDefinition::getType,
                        Function.identity()
                ));
    }

    public void clear() {
        beanDefinitionMap.clear();
    }

    public Map<Class<?>, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }

    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.values().stream()
                .map(BeanDefinition::getType)
                .collect(Collectors.toSet());
    }

    public void registerBeanDefinition(final Class<?> clazz, final BeanDefinition beanDefinition) {
        if (beanDefinitionMap.values().stream().anyMatch(definition -> definition.hasSameName(beanDefinition))) {
            throw new IllegalArgumentException("bean %s already exists for %s".formatted(beanDefinition.getBeanClassName(), clazz.getName()));
        }
        this.beanDefinitionMap.put(clazz, beanDefinition);
    }

    public BeanDefinition getBeanDefinition(Class<?> beanClass) {
        final BeanDefinition beanDefinition = beanDefinitionMap.get(beanClass);
        if (beanDefinition != null) {
            return beanDefinition;
        }

        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(beanClass, getBeanClasses())
                .orElseThrow(() -> new BeanInstantiationException(beanClass, "Could not autowire. No concrete class found for %s.".formatted(beanClass.getName())));

        final BeanDefinition concreteBeanDefinition = beanDefinitionMap.get(concreteClass);
        if (concreteBeanDefinition == null) {
            throw new BeanInstantiationException(beanClass, "cannot find bean for " + beanClass.getName());
        }

        return concreteBeanDefinition;
    }

    public void mergeBeanDefinitions(final BeanDefinitions beanDefinitions) {
        final Map<Class<?>, BeanDefinition> beanDefinitionMap = beanDefinitions.getBeanDefinitionMap();
        for (final BeanDefinition beanDefinition : beanDefinitionMap.values()) {
            registerBeanDefinition(beanDefinition.getType(), beanDefinition);
        }
    }
}
