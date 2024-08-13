package com.interface21.context.support;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.beans.factory.config.GenericBeanDefinition;
import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ConfigurationScanner implements BeanScanner {

    private final List<Class<?>> configurationClasses;

    public ConfigurationScanner(Class<?>... configurationClasses) {
        this.configurationClasses = List.of(configurationClasses);
    }

    @Override
    public Map<Class<?>, BeanDefinition> scan() {
        Object[] basePackages = getBasePackages();
        return findBeanMethods(new Reflections(basePackages));
    }

    private Object[] getBasePackages() {
        return configurationClasses.stream()
                                   .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
                                   .map(clazz -> clazz.getAnnotation(ComponentScan.class).basePackages())
                                   .toArray();
    }

    private Map<Class<?>, BeanDefinition> findBeanMethods(Reflections reflections) {
        Map<Class<?>, BeanDefinition> beanDefinitions = new HashMap<>();
        Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(Configuration.class);

        for (Class<?> configClass : configClasses) {
            beanDefinitions.put(configClass, GenericBeanDefinition.from(configClass));
            beanDefinitions.putAll(getBeanDefinitionsFromConfigClass(configClass));
        }

        return beanDefinitions;
    }

    private Map<Class<?>, BeanDefinition> getBeanDefinitionsFromConfigClass(Class<?> configClass) {
        Map<Class<?>, BeanDefinition> beanDefinitions = new HashMap<>();
        Set<Method> beanMethods = BeanFactoryUtils.getBeanMethods(configClass, Bean.class);

        for (Method beanMethod : beanMethods) {
            Class<?> returnType = beanMethod.getReturnType();
            ConfigurationBeanDefinition beanDefinition = new ConfigurationBeanDefinition(returnType, beanMethod);
            beanDefinitions.put(returnType, beanDefinition);
        }

        return beanDefinitions;
    }}
