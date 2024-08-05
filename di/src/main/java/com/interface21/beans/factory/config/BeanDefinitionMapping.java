package com.interface21.beans.factory.config;

import com.interface21.beans.BeanScanner;
import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanDefinitionMapping {
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private final BeanScanner scanner;

    public BeanDefinitionMapping(final String... basePackages) {
        this.scanner = new BeanScanner((Object) basePackages);
    }

    public void scanBeanDefinitions() {
        final Set<Class<?>> beanClasses = scanner.scanClassesTypeAnnotatedWith();
        toBeanDefinitionMap(beanClasses);
    }

    private void toBeanDefinitionMap(final Set<Class<?>> beanClasses) {
        beanClasses.forEach(this::put);
    }

    private void put(final Class<?> beanClass) {
        putBeanMethods(beanClass);

        beanDefinitionMap.put(beanClass.getName(), new RootBeanDefinition(beanClass, beanClass.getName()));
    }

    private void putBeanMethods(final Class<?> beanClass) {
        BeanFactoryUtils.getBeanMethods(beanClass, Bean.class).forEach(beanMethod -> {
            final ConfigurationClassBeanDefinition beanDefinition = new ConfigurationClassBeanDefinition(beanMethod, beanClass);

            beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
        });
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
