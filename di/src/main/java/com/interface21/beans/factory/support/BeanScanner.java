package com.interface21.beans.factory.support;

import com.interface21.context.stereotype.Component;
import org.reflections.Reflections;

import java.util.List;
import java.util.stream.Collectors;

public class BeanScanner {

    private static final String STEREOTYPE_PACKAGE = "com.interface21.context.stereotype";

    public static List<Class<?>> getBeansWithAnnotation(final String... basePackages) {
        final Reflections reflections = new Reflections(STEREOTYPE_PACKAGE, basePackages);
        return reflections.getTypesAnnotatedWith(Component.class)
                .stream()
                .filter(clazz -> !clazz.isAnnotation())
                .collect(Collectors.toUnmodifiableList());
    }
}
