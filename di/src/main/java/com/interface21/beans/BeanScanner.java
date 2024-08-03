package com.interface21.beans;

import com.interface21.context.stereotype.Component;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner(final Object... basePackages) {
        reflections = new Reflections(addDefaultPackage(basePackages));
    }

    private Object[] addDefaultPackage(final Object[] basePackages) {
        final Object[] params = new Object[basePackages.length + 1];
        System.arraycopy(basePackages, 0, params, 0, basePackages.length);
        params[basePackages.length] = "com.interface21";
        return params;
    }

    public Set<Class<?>> scanClassesTypeAnnotatedWith() {
        return reflections.getTypesAnnotatedWith(Component.class)
                .stream()
                .filter(this::isClass)
                .collect(Collectors.toSet());
    }

    private boolean isClass(final Class<?> type) {
        return !type.isAnnotation() && !type.isInterface();
    }
}
