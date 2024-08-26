package com.interface21.beans.factory.support;

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
        configurationClasses.stream()
                .map(ConfigurationBeanDefinition::new)
                .forEach(beanDefinition -> this.registry.registerBeanDefinition(beanDefinition.getType(), beanDefinition));
    }
}
