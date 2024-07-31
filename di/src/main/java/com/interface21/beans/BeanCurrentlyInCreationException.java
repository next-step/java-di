package com.interface21.beans;

import java.util.Collection;
import java.util.StringJoiner;

public class BeanCurrentlyInCreationException extends RuntimeException {
    public BeanCurrentlyInCreationException(final Collection<Class<?>> classes) {
        super("Circular dependency detected: " + describeCircularDependency(classes));
    }

    private static String describeCircularDependency(final Collection<Class<?>> classes) {
        if (classes.isEmpty()) {
            return "";
        }
        final StringJoiner stringJoiner = new StringJoiner(" ---> ");
        classes.forEach(clazz -> stringJoiner.add(clazz.getName()));
        stringJoiner.add(classes.iterator().next().getName());

        return stringJoiner.toString();
    }
}
