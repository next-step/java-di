package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanDefinitions {
    private final Map<String, BeanDefinition> nameToBeanDefinitionMap = new HashMap<>();

    public void registerBeanDefinitions(Set<Class<?>> beanClasses) {
        for (Class<?> beanClass : beanClasses) {
            DefaultBeanDefinition beanDefinition = new DefaultBeanDefinition(beanClass);
            nameToBeanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
        }
    }

    public Set<Class<?>> extractTypes() {
        return nameToBeanDefinitionMap.values()
                .stream()
                .map(BeanDefinition::getType)
                .collect(Collectors.toSet());
    }

    public <T> boolean isNotRegistered(Class<T> clazz) {
        return !isRegistered(clazz);
    }

    public <T> boolean isRegistered(Class<T> clazz) {
        return extractTypes()
                .stream()
                .anyMatch(type -> {
                    Set<Class<?>> interfaces = Set.of(type.getInterfaces());
                    return type == clazz || interfaces.contains(clazz);
                });

    }
}
