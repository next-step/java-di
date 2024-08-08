package com.interface21.beans.factory.config;

import com.interface21.beans.BeanScanner;
import com.interface21.beans.factory.support.BeanDefinitionRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanDefinitionMapping implements BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final BeanScanner scanner;

    public BeanDefinitionMapping(final String... basePackages) {
        this.scanner = new BeanScanner((Object) basePackages);
    }

    @Override
    public void registerBeanDefinition(final Class<?> clazz, final BeanDefinition beanDefinition) {
        beanDefinitionMap.put(clazz.getName(), beanDefinition);
    }

    public void scanBeanDefinitions() {
        final Set<Class<?>> beanClasses = scanner.scanClassesTypeAnnotatedWith();
        toBeanDefinitionMap(beanClasses);
    }

    private void toBeanDefinitionMap(final Set<Class<?>> beanClasses) {
        beanClasses.forEach(this::put);
    }

    private void put(final Class<?> beanClass) {
        registerBeanDefinition(beanClass, new RootBeanDefinition(beanClass, beanClass.getName()));
    }

    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.values().stream().map(BeanDefinition::getType).collect(Collectors.toUnmodifiableSet());
    }

    public void clear() {
        beanDefinitionMap.clear();
    }

    public BeanDefinition getBeanDefinition(final Class<?> clazz) {
        return beanDefinitionMap.get(clazz.getName());
    }
}
