package com.interface21.beans.factory.support;

import com.interface21.beans.BeanDefinitionException;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.SimpleBeanDefinition;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap;

    public SimpleBeanDefinitionRegistry() {
        this(new HashSet<>());
    }

    public SimpleBeanDefinitionRegistry(final Set<Class<?>> beanClasses) {
        beanDefinitionMap = beanClasses.stream()
                .map(SimpleBeanDefinition::from)
                .collect(Collectors.toMap(
                        SimpleBeanDefinition::getBeanClassName,
                        Function.identity()
                ));
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.values().stream()
                .map(BeanDefinition::getType)
                .collect(Collectors.toSet());
    }

    @Override
    public BeanDefinition getBeanDefinition(final Class<?> clazz) {
        return beanDefinitionMap.values().stream()
                .filter(beanDefinition -> beanDefinition.getType().equals(clazz))
                .findFirst()
                .orElseGet(() -> findByConcreteClass(clazz));
    }

    private BeanDefinition findByConcreteClass(final Class<?> clazz) {
        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses())
                .orElseThrow(() -> new BeanDefinitionException("Could not autowire. No concrete class found for %s.".formatted(clazz.getName())));
        return beanDefinitionMap.values().stream()
                .filter(beanDefinition -> beanDefinition.getType().equals(concreteClass))
                .findFirst()
                .orElseThrow(() -> new BeanDefinitionException("cannot find bean for " + clazz.getName()));
    }

    @Override
    public void clear() {
        beanDefinitionMap.clear();
    }

    @Override
    public void registerBeanDefinition(final Class<?> clazz, final BeanDefinition beanDefinition) {
        if (beanDefinitionMap.values().stream().anyMatch(definition -> definition.hasSameName(beanDefinition))) {
            throw new BeanDefinitionException("bean %s already exists for %s".formatted(beanDefinition.getBeanClassName(), clazz.getName()));
        }
        this.beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
    }

    @Override
    public void mergeBeanDefinitionRegistry(final BeanDefinitionRegistry beanDefinitionRegistry) {
        final Map<String, BeanDefinition> beanDefinitions = beanDefinitionRegistry.getBeanDefinitions();
        for (final BeanDefinition beanDefinition : beanDefinitions.values()) {
            registerBeanDefinition(beanDefinition.getType(), beanDefinition);
        }
    }

    @Override
    public Map<String, BeanDefinition> getBeanDefinitions() {
        return Collections.unmodifiableMap(beanDefinitionMap);
    }
}
