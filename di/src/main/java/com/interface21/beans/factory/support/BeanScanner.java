package com.interface21.beans.factory.support;

import com.interface21.context.annotation.Configuration;
import com.interface21.context.stereotype.Component;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class BeanScanner {
    private static final String STEREOTYPE_PACKAGE = "com.interface21.context.stereotype";

    private final Reflections reflections;

    public BeanScanner(String... basePackages) {
        this.reflections = new Reflections(STEREOTYPE_PACKAGE, basePackages);
    }

    public Set<Class<?>> scanComponent() {
        return reflections.getTypesAnnotatedWith(Component.class)
                .stream()
                .filter(Predicate.not(Class::isAnnotation))
                .collect(Collectors.toSet());
    }

    public Set<Class<?>> scanConfiguration() {
        return new HashSet<>(reflections.getTypesAnnotatedWith(Configuration.class));
    }
}
