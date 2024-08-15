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
import java.util.stream.Stream;

public class ConfigurationBeanScanner {
    private static final Class<? extends Annotation> CONFIGURATION_ANNOTATION = Configuration.class;

    private final BeanDefinitionRegistry beanFactory;

    private String[] basePackages;

    public ConfigurationBeanScanner(BeanDefinitionRegistry beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register(Class<?> initialConfigurationClass) {
        collectBasePackages(initialConfigurationClass.getPackageName());

        registerBeanDefinitions();
    }

    private void collectBasePackages(String... initialBasePackage) {
        Set<Class<?>> configurations = ReflectionUtils.getTypesAnnotatedWith(initialBasePackage, CONFIGURATION_ANNOTATION);
        Stream<String> additionalBasePackages = configurations
                .stream()
                .filter(configuration -> configuration.isAnnotationPresent(ComponentScan.class))
                .flatMap(configuration -> Arrays.stream(configuration.getAnnotation(ComponentScan.class).value()))
                .distinct();

        basePackages = Stream.concat(
                Arrays.stream(initialBasePackage),
                additionalBasePackages
        ).toArray(String[]::new);
    }

    private void registerBeanDefinitions() {
        ReflectionUtils.getTypesAnnotatedWith(basePackages, CONFIGURATION_ANNOTATION)
                       .forEach(this::registerBeanDefinitions);
    }

    private void registerBeanDefinitions(Class<?> configurationClass) {
        Arrays.stream(configurationClass.getDeclaredMethods())
              .filter(this::isBeanMethod)
              .forEach(this::registerBeanDefinition);
    }

    private void registerBeanDefinition(Method method) {
        beanFactory.registerBeanDefinition(method.getReturnType(), new ConfigurationBeanDefinition(method));
    }

    private boolean isBeanMethod(final Method beanMethod) {
        return beanMethod.isAnnotationPresent(Bean.class) &&
                Modifier.isPublic(beanMethod.getModifiers());
    }

    public String[] getBasePackages() {
        return basePackages;
    }
}
