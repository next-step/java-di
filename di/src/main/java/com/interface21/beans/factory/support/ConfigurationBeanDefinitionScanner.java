package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import com.interface21.context.annotation.Bean;

import java.util.Set;

public class ConfigurationBeanDefinitionScanner implements BeanDefinitionScanner {

    private final BeanDefinitionRegistry registry;

    public ConfigurationBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public int scan(String[] basePackages) {
        registerBeanDefinitions(BeanScanner.scanConfiguration(basePackages));
        return this.registry.getBeanDefinitionCount();
    }

    private void registerBeanDefinitions(Set<Class<?>> configurationClasses) {
        registerDeclaringClass(configurationClasses);
        registerBeanMethod(configurationClasses);
    }

    private void registerDeclaringClass(Set<Class<?>> configurationClasses) {
        configurationClasses.stream()
                .map(AnnotationBeanDefinition::new)
                .forEach(beanDefinition -> this.registry.registerBeanDefinition(beanDefinition.getType(), beanDefinition));
    }

    private void registerBeanMethod(Set<Class<?>> configurationClasses) {
        configurationClasses.stream()
                .flatMap(clazz -> BeanFactoryUtils.getBeanMethods(clazz, Bean.class).stream())
                .map(method -> new ConfigurationBeanDefinition(method.getReturnType(), method))
                .forEach(beanDefinition -> this.registry.registerBeanDefinition(beanDefinition.getType(), beanDefinition));
    }
}
