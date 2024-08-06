package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.beans.factory.config.SimpleBeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class NewConfigurationBeanScanner {
    private final List<Class<?>> configurationClasses;
    private final NewBeanDefinitionRegistry beanDefinitionRegistry;

    public NewConfigurationBeanScanner(final NewBeanDefinitionRegistry beanDefinitionRegistry) {
        this.configurationClasses = new ArrayList<>();
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void register(final Class<?> configClass) {
        if (!configClass.isAnnotationPresent(Configuration.class)) {
            throw new IllegalArgumentException("configuration classes are not annotated with @Configuration");
        }
        configurationClasses.add(configClass);
        beanDefinitionRegistry.registerBeanDefinition(configClass, SimpleBeanDefinition.from(configClass));
        Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> beanDefinitionRegistry.registerBeanDefinition(method.getReturnType(), ConfigurationBeanDefinition.from(method)));
    }

    public Object[] getBasePackages() {
        return configurationClasses.stream()
                .filter(configClass -> configClass.isAnnotationPresent(ComponentScan.class))
                .flatMap(configClass -> getBasePackages(configClass).stream())
                .toArray();
    }

    private List<String> getBasePackages(final Class<?> configClass) {
        final ComponentScan annotation = configClass.getAnnotation(ComponentScan.class);
        final List<String> basePackages = Stream.concat(Arrays.stream(annotation.value()), Arrays.stream(annotation.basePackages())).toList();
        if (basePackages.isEmpty()) {
            return List.of(configClass.getPackageName());
        }
        return basePackages;
    }
}
