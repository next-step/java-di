package com.interface21.beans.factory.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanDefinitionMapping {
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    public void toBeanDefinitionMap(final Set<Class<?>> beanClasses) {
        beanClasses.forEach(this::put);
    }

    private void put(final Class<?> beanClass) {
        beanDefinitionMap.put(beanClass.getName(), new RootBeanDefinition(beanClass, beanClass.getName()));
    }

    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.values().stream().map(BeanDefinition::getType).collect(Collectors.toUnmodifiableSet());
    }

    public void clear() {
        beanDefinitionMap.clear();
    }
}
