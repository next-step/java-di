package com.interface21.beans.factory.support;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import com.interface21.context.stereotype.Component;

public final class BeanScanner {

    public static final String STEREOTYPE_PACKAGE = "com.interface21.context.stereotype";

    private BeanScanner() {}

    public static Set<Class<?>> scanBeans(String[] baskPackage) {
        final Reflections reflections = new Reflections(STEREOTYPE_PACKAGE, baskPackage);
        return reflections.getTypesAnnotatedWith(Component.class).stream()
                .filter(clazz -> !clazz.isAnnotation())
                .collect(Collectors.toUnmodifiableSet());
    }

    public static Set<Class<?>> scanBeans(
            Class<? extends Annotation> annotationType, String... basePackages) {
        return scanBeans(basePackages).stream()
                .filter(it -> it.isAnnotationPresent(annotationType))
                .collect(Collectors.toUnmodifiableSet());
    }
}
