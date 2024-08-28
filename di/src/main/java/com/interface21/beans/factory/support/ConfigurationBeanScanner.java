package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;

import java.util.Set;

public class ConfigurationBeanScanner implements BeanDefinitionScanner {

    private final BeanDefinitionRegistry registry;

    public ConfigurationBeanScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void register(Class<?> clazz) {
        scan(BeanScanner.scanBasePackages(clazz));
    }

    @Override
    public int scan(String[] basePackages) {
        Set<Class<?>> configClasses = registerConfigurationClass(BeanScanner.scanBeans(Configuration.class, basePackages));
        registerBeanDefinitions(configClasses);
        return this.registry.getBeanDefinitionCount();
    }


    private Set<Class<?>> registerConfigurationClass(Set<Class<?>> configurationClasses) {
        configurationClasses.stream()
                .map(AnnotationBeanDefinition::new)
                .forEach(bd -> this.registry.registerBeanDefinition(bd.getType(), bd));
        return configurationClasses;
    }

    private void registerBeanDefinitions(Set<Class<?>> configurationClasses) {
        configurationClasses.stream()
                .flatMap(clazz -> BeanFactoryUtils.getBeanMethods(clazz, Bean.class).stream())
                .map(method -> new ConfigurationBeanDefinition(method.getReturnType(), method))
                .forEach(beanDefinition -> this.registry.registerBeanDefinition(beanDefinition.getType(), beanDefinition));
    }
}
