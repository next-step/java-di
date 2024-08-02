package com.interface21.beans.factory.support;

import com.interface21.context.stereotype.Component;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class BeanScanner {
    private static final String STEREOTYPE_PACKAGE = "com.interface21.context.stereotype";

    private final String[] basePackages;

    public BeanScanner(String... basePackages) {
        this.basePackages = basePackages;
    }

    public Set<Class<?>> scan() {
        Reflections reflections = new Reflections(STEREOTYPE_PACKAGE, basePackages);
        return reflections.getTypesAnnotatedWith(Component.class)
                .stream()
                .filter(Predicate.not(Class::isAnnotation))
                .collect(Collectors.toSet());
    }
}
