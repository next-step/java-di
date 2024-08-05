package com.interface21.beans.factory.support;

import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;

import java.util.List;

public class ConfigurationScanner {
    private final List<Class<?>> configurationClasses;

    public ConfigurationScanner(final List<Class<?>> configurationClasses) {
        validate(configurationClasses);
        this.configurationClasses = configurationClasses;
    }

    private void validate(final List<Class<?>> configurationClasses) {
        if (configurationClasses.stream().noneMatch(clazz -> clazz.isAnnotationPresent(Configuration.class))) {
            throw new IllegalArgumentException("configuration classes are not annotated with @Configuration");
        }
    }

    public Object[] getBasePackages() {
        return configurationClasses.stream()
                .filter(configClass -> configClass.isAnnotationPresent(ComponentScan.class))
                .flatMap(configClass -> getBasePackages(configClass).stream())
                .toArray();
    }

    private List<String> getBasePackages(final Class<?> configClass) {
        final ComponentScan annotation = configClass.getAnnotation(ComponentScan.class);
        if (annotation.value().length > 0) {
            return List.of(annotation.value());
        } else if (annotation.basePackages().length > 0) {
            return List.of(annotation.basePackages());
        }
        return List.of(configClass.getName());
    }
}
