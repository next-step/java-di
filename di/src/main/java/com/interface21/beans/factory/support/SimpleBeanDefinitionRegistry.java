package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.DefaultBeanDefinition;
import com.interface21.beans.factory.exception.BeanException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SimpleBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public SimpleBeanDefinitionRegistry(final List<Class<?>> beans) {
        beans.forEach(bean -> {
            final BeanDefinition beanDefinition = new DefaultBeanDefinition(bean);
            this.registerBeanDefinition(bean, beanDefinition);
        });
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        if (clazz == null || beanDefinition == null) {
            throw new BeanException("Class and bean definition must not be null");
        }

        final String className = clazz.getName();

        if (beanDefinitionMap.containsKey(className)) {
            throw new BeanException("Bean definition already registered for class: " + className);
        }

        this.beanDefinitionMap.put(className, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(Class<?> clazz) {
        final String className = clazz.getName();
        final BeanDefinition beanDefinition = this.beanDefinitionMap.get(clazz.getName());
        if (beanDefinition == null) {
            throw new BeanException("No bean definition found for class: " + className);
        }
        return beanDefinition;
    }

    @Override
    public void removeBeanDefinition(Class<?> clazz) {
        final String className = clazz.getName();

        if (this.beanDefinitionMap.remove(className) == null) {
            throw new BeanException("No bean definition found for class: " + className);
        }
    }

    @Override
    public boolean containsBeanDefinition(Class<?> clazz) {
        return this.beanDefinitionMap.containsKey(clazz.getName());
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return this.beanDefinitionMap.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return getBeanDefinitions().stream()
                .map(BeanDefinition::getType)
                .collect(Collectors.toSet());
    }
}
