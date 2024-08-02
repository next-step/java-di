package com.interface21.beans;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner(final Object... basePackages) {
        reflections = new Reflections(basePackages, "com.interface21");
    }

    public Set<Class<?>> scanClassesTypeAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation)
                .stream()
                .filter(this::isClass)
                .collect(Collectors.toSet());
    }

    private boolean isClass(final Class<?> type) {
        return !type.isAnnotation() && !type.isInterface();
    }
}
