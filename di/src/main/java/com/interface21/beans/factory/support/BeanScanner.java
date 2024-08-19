package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.DefaultBeanDefintion;
import com.interface21.context.stereotype.Component;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class BeanScanner implements Scanner<Object> {

    private final BeanDefinitionRegistry registry;

    public BeanScanner(BeanDefinitionRegistry registry) {

        this.registry = registry;
    }

    @Override
    public void scan(Object... basePackage) {
        Set<Class<?>> componentAnnotations = scanAnnotationWithComponent();

        Reflections beanScanner = new Reflections(basePackage);
        for (Class<?> subType : componentAnnotations) {
            beanScanner.getTypesAnnotatedWith((Class<? extends Annotation>) subType)
                .stream()
                .forEach(component -> registry.registerBeanDefinition(component,
                    new DefaultBeanDefintion(component)));
        }

    }

    private Set<Class<?>> scanAnnotationWithComponent() {
        Reflections reflections = new Reflections("com.interface21.context");
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Component.class);

        return annotatedClasses.stream()
            .filter(Class::isAnnotation)
            .collect(Collectors.toSet());
    }

}
