package com.interface21.beans;

import com.interface21.beans.factory.config.BeanDefinition;

import java.util.Collection;
import java.util.StringJoiner;

public class BeanCurrentlyInCreationException extends RuntimeException {
    public BeanCurrentlyInCreationException(final Collection<BeanDefinition> beanDefinitions) {
        super("Circular dependency detected: " + describeCircularDependency(beanDefinitions));
    }

    private static String describeCircularDependency(final Collection<BeanDefinition> beanDefinitions) {
        if (beanDefinitions.isEmpty()) {
            return "";
        }
        final StringJoiner stringJoiner = new StringJoiner(" ---> ");
        beanDefinitions.forEach(beanDefinition -> stringJoiner.add(beanDefinition.getBeanClassName()));
        stringJoiner.add(beanDefinitions.iterator().next().getBeanClassName());

        return stringJoiner.toString();
    }
}
