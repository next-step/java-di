package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.beans.factory.config.SingletonBeanDefinition;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import com.interface21.context.stereotype.Component;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class ConfigurationScanner {

    private final List<Class<?>> configurations;

    public ConfigurationScanner(List<Class<?>> configurations) {
        this.configurations = configurations;
    }

    public Map<Class<?>, BeanDefinition> scanBean() {
        Object[] basePackages = getScanPackage();
        Reflections reflections = new Reflections(basePackages, Scanners.TypesAnnotated, Scanners.SubTypes);
        return Stream.concat(
                parseSingletonBeans(reflections).entrySet().stream(),
                parseConfigurationBeans(reflections).entrySet().stream()
        ).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Object[] getScanPackage() {
        return configurations.stream()
                .filter(configuration -> configuration.isAnnotationPresent(ComponentScan.class))
                .map(configuration -> configuration.getAnnotation(ComponentScan.class))
                .map(ComponentScan::value)
                .flatMap(Arrays::stream)
                .toArray();
    }

    private Map<Class<?>, BeanDefinition> parseSingletonBeans(Reflections reflections) {
        return Stream.of(Component.class, Controller.class, Service.class, Repository.class)
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .collect(toMap(clazz -> clazz, SingletonBeanDefinition::new));
    }

    private Map<Class<?>, BeanDefinition> parseConfigurationBeans(Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Configuration.class)
                .stream()
                .collect(toMap(clazz -> clazz, ConfigurationBeanDefinition::from));
    }
}
