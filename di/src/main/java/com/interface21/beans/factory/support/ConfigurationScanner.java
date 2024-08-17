package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class ConfigurationScanner implements Scanner {
    private final BeanDefinitionRegistry registry;

    public ConfigurationScanner(BeanDefinitionRegistry registry) {

        this.registry = registry;
    }

    @Override
    public void scan(Object... basePackage) {

        Reflections beanScanner = new Reflections(basePackage);
        List<Class<?>> beanClasses = beanScanner.getTypesAnnotatedWith(Configuration.class)
                .stream()
                .toList();

        beanClasses.forEach(
                beanClazz -> registerBeanMethod(beanClazz)
        );

    }

    public void registerBeanMethod(Class<?> clazz) {
        Set<Method> methods = BeanFactoryUtils.getBeanMethods(clazz, Bean.class);

        methods.stream()
                .forEach(method -> registry.registerBeanDefinition(method.getReturnType(),
                        new ConfigurationBeanDefinition(method)));

    }
}
