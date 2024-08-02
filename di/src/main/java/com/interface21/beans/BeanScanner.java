package com.interface21.beans;

import com.interface21.context.stereotype.Component;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner(final Object... basePackages) {
        if (basePackages.length == 0) {
            throw new BeanScannerException("basePackages cannot be empty");
        }
        reflections = new Reflections(basePackages);
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
