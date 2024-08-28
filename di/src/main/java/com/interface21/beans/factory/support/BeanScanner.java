package com.interface21.beans.factory.support;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import org.reflections.Reflections;

import com.interface21.context.stereotype.Component;

public final class BeanScanner {

    private static final List<Class<? extends Annotation>> BEAN_ANNOTATIONS = List.of(Component.class, Service.class, Controller.class, Repository.class, Configuration.class);

    private BeanScanner() {}

    public static Set<Class<?>> scanBeans(String[] baskPackage) {
        return BEAN_ANNOTATIONS.stream()
                .flatMap(annotation -> scanBeans(annotation, baskPackage).stream())
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> scanBeans(
            Class<? extends Annotation> annotationType, String[] basePackages) {
        final Reflections reflections = new Reflections(basePackages);
        return reflections.getTypesAnnotatedWith(annotationType).stream()
                .filter(clazz -> !clazz.isAnnotation())
                .collect(Collectors.toUnmodifiableSet());
    }

    public static Set<Class<?>> scanConfiguration(String[] basePackages) {
        return scanBeans(Configuration.class, basePackages);
    }

    public static String[] scanBasePackages(Class<?> configuration) {
        ComponentScan componentScan = configuration.getAnnotation(ComponentScan.class);
        return componentScan.value();
    }
}
