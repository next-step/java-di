package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import com.interface21.core.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class ConfigurationBeanScanner {
    private static final Class<? extends Annotation> CONFIGURATION_ANNOTATION = Configuration.class;

    private final BeanDefinitionRegistry beanFactory;

    public ConfigurationBeanScanner(BeanDefinitionRegistry beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register() {
        getAllConfigurationClasses().forEach(this::registerBeanDefinitions);
    }

    private Set<Class<?>> getAllConfigurationClasses() {
        return ReflectionUtils.getAllTypesAnnotatedWith(CONFIGURATION_ANNOTATION);
    }

    private void registerBeanDefinitions(Class<?> configurationClass) {
        Arrays.stream(configurationClass.getDeclaredMethods())
                .filter(this::isBeanMethod)
                .forEach(this::registerBeanDefinition);
    }

    private boolean isBeanMethod(final Method beanMethod) {
        return beanMethod.isAnnotationPresent(Bean.class) &&
                Modifier.isPublic(beanMethod.getModifiers());
    }

    private void registerBeanDefinition(Method method) {
        beanFactory.registerBeanDefinition(method.getReturnType(), new ConfigurationBeanDefinition(method));
    }

    public String[] getBasePackages() {
        return getAllConfigurationClasses()
                .stream()
                .flatMap(this::extractBasePackages)
                .distinct()
                .toArray(String[]::new);
    }

    private Stream<String> extractBasePackages(Class<?> configuration) {
        Stream<String> packageNameStream = Stream.of(configuration.getPackageName());

        ComponentScan annotation = configuration.getAnnotation(ComponentScan.class);
        if (annotation == null) {
            return packageNameStream;
        }

        String[] basePackages = annotation.basePackages();
        String[] value = annotation.value();
        return Stream.of(packageNameStream, Arrays.stream(basePackages), Arrays.stream(value))
                .flatMap(Function.identity());
    }
}
