package com.interface21.context.support;

import com.interface21.beans.BeanUtils;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import com.interface21.core.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanScanner {
    public static final Class<? extends Annotation>[] BEAN_CLASS_ANNOTATIONS = new Class[]{
            Repository.class,
            Service.class,
            Controller.class};

    public static final Class<Configuration> CONFIGURATION_ANNOTATION = Configuration.class;

    private final String[] initialBasePackage;
    private String[] basePackages;

    public BeanScanner(final String... initialBasePackages) {
        this.initialBasePackage = initialBasePackages;
    }

    public void initialize() {
        basePackages = collectBasePackages();
    }

    private String[] collectBasePackages() {
        Set<Class<?>> configurations = ReflectionUtils.getTypesAnnotatedWith(initialBasePackage, CONFIGURATION_ANNOTATION);
        Stream<String> additionalBasePackages = configurations
                .stream()
                .filter(configuration -> configuration.isAnnotationPresent(ComponentScan.class))
                .flatMap(configuration -> Arrays.stream(configuration.getAnnotation(ComponentScan.class).value()))
                .distinct();

        return Stream.concat(
                Arrays.stream(initialBasePackage),
                additionalBasePackages
        ).toArray(String[]::new);
    }

    // visible for testing
    String[] getBasePackages() {
        return basePackages;
    }

    public Set<Class<?>> scanBeanClasses() {
        return ReflectionUtils.getTypesAnnotatedWith(basePackages, BEAN_CLASS_ANNOTATIONS);
    }

    public List<ConfigurationInstanceAndMethod> scanConfigurationBeans() {
        Set<Class<?>> configurations = ReflectionUtils.getTypesAnnotatedWith(basePackages, CONFIGURATION_ANNOTATION);
        return configurations
                .stream()
                .flatMap(configurationClass -> {
                    Object object = BeanUtils.instantiate(configurationClass);
                    Method[] declaredMethods = configurationClass.getDeclaredMethods();

                    return Arrays.stream(declaredMethods)
                                 .filter(BeanScanner::isBeanMethod)
                                 .map(method -> new ConfigurationInstanceAndMethod(object, method));
                })
                .toList();
    }

    public Collection<Class<?>> scanConfigurationBeanTypes() {
        return scanConfigurationBeans()
                .stream()
                .map((ConfigurationInstanceAndMethod t) -> t.method().getReturnType())
                .collect(Collectors.toUnmodifiableList());
    }

    private static boolean isBeanMethod(final Method beanMethod) {
        return beanMethod.isAnnotationPresent(Bean.class) &&
                Modifier.isPublic(beanMethod.getModifiers());
    }
}
