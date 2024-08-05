package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    public BeanDefinitionRegistry scanBean() {
        final BeanDefinitionRegistry beanDefinitionRegistry = new SimpleBeanDefinitionRegistry();
        final Reflections reflections = new Reflections(getBasePackages());
        final Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(Configuration.class);
        configClasses.stream()
                .flatMap(configClass -> Arrays.stream(configClass.getMethods())
                        .filter(method -> method.isAnnotationPresent(Bean.class)))
                .forEach(method -> beanDefinitionRegistry.registerBeanDefinition(method.getReturnType(), ConfigurationBeanDefinition.from(method)));
        return beanDefinitionRegistry;
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
        return List.of(configClass.getPackageName());
    }
}
