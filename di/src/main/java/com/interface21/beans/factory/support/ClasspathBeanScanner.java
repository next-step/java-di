package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.DefaultBeanDefintion;
import com.interface21.context.stereotype.Component;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ClasspathBeanScanner implements Scanner {

    public static final String LIB = "com.interface21";
    private final BeanDefinitionRegistry registry;

    public ClasspathBeanScanner(BeanDefinitionRegistry registry) {

        this.registry = registry;
    }

    @Override
    public void scan(Object... basePackage) {
        Reflections reflections = new Reflections(LIB, basePackage);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Component.class);
        Set<Class<?>> annotations = annotatedClasses.stream()
            .filter(Class::isAnnotation)
            .collect(Collectors.toSet());

        Reflections beanScanner = new Reflections(basePackage);
        for (Class<?> subType : annotations) {
            beanScanner.getTypesAnnotatedWith((Class<? extends Annotation>) subType)
                .stream()
                .forEach(component -> registry.registerBeanDefinition(component,
                    new DefaultBeanDefintion(component)));
        }

    }

}
