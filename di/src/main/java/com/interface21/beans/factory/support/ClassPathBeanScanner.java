package com.interface21.beans.factory.support;

import com.interface21.context.stereotype.Component;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ClassPathBeanScanner {
    private static final String STEREOTYPE_PACKAGE = "com.interface21.context.stereotype";

    private final Reflections reflections;

    public ClassPathBeanScanner(String... basePackages) {
        this.reflections = new Reflections(STEREOTYPE_PACKAGE, basePackages);
    }

    public void registerBeanDefinitions(BeanDefinitions beanDefinitions) {
        Set<Class<?>> componentClasses = scan();
        for (Class<?> componentClass : componentClasses) {
            beanDefinitions.register(new ComponentBeanDefinition(componentClass));
        }
    }

    private Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(Component.class)
                .stream()
                .filter(Predicate.not(Class::isAnnotation))
                .collect(Collectors.toSet());
    }
}
