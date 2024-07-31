package com.interface21.beans.factory.support;

import com.interface21.context.stereotype.Component;
import java.util.List;
import java.util.function.Predicate;
import org.reflections.Reflections;

public class BeanScanner {
    private static final String STEREOTYPE_PACKAGE = "com.interface21.context.stereotype";

    private final String[] basePackages;

    public BeanScanner(String... basePackages) {
        this.basePackages = basePackages;
    }

    public List<Class<?>> scan() {
        Reflections reflections = new Reflections(STEREOTYPE_PACKAGE, basePackages);
        return reflections.getTypesAnnotatedWith(Component.class)
                .stream()
                .filter(Predicate.not(Class::isAnnotation))
                .toList();
    }
}
