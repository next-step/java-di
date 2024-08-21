package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinitions;
import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ConfigurationBeanScanner implements BeanDefinitionScanner {
    private final List<Class<?>> configurationClasses;

    public ConfigurationBeanScanner(final List<Class<?>> configurationClasses) {
        validate(configurationClasses);
        this.configurationClasses = configurationClasses;
    }

    private void validate(final List<Class<?>> configurationClasses) {
        if (configurationClasses.stream().noneMatch(clazz -> clazz.isAnnotationPresent(Configuration.class))) {
            throw new IllegalArgumentException("configuration classes are not annotated with @Configuration");
        }
    }

    @Override
    public BeanDefinitions scan() {
        final BeanDefinitions beanDefinitions = new BeanDefinitions();
        final Reflections reflections = new Reflections(getBasePackages());
        final Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(Configuration.class);
        configClasses.stream()
                .flatMap(configClass -> Arrays.stream(configClass.getMethods())
                        .filter(method -> method.isAnnotationPresent(Bean.class)))
                .forEach(method -> beanDefinitions.registerBeanDefinition(method.getReturnType(), ConfigurationBeanDefinition.from(method)));
        return beanDefinitions;
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
