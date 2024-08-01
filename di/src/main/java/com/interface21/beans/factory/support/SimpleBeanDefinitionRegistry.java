package com.interface21.beans.factory.support;

import com.interface21.beans.BeanDefinitionException;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.SimpleBeanDefinition;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private final Map<Class<?>, BeanDefinition> beanDefinitionMap;

    public SimpleBeanDefinitionRegistry() {
        this(new HashSet<>());
    }

    public SimpleBeanDefinitionRegistry(final Set<Class<?>> beanClasses) {
        beanDefinitionMap = beanClasses.stream()
                .map(SimpleBeanDefinition::from)
                .collect(Collectors.toMap(
                        SimpleBeanDefinition::getType,
                        Function.identity()
                ));
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.keySet();
    }

    @Override
    public Constructor<?> getBeanConstructor(final Class<?> clazz) {
        final BeanDefinition beanDefinition = beanDefinitionMap.get(clazz);
        if (beanDefinition == null) {
            throw new BeanDefinitionException("cannot find bean for " + clazz.getName());
        }
        return beanDefinition.getConstructor();
    }

    @Override
    public void clear() {
        beanDefinitionMap.clear();
    }

    @Override
    public void registerBeanDefinition(final Class<?> clazz, final BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(clazz, beanDefinition);
    }
}
