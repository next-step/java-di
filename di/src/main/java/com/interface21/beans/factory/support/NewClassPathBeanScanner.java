package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.SimpleBeanDefinition;
import com.interface21.context.stereotype.Component;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public class NewClassPathBeanScanner {
    private static final List<Class<? extends Annotation>> beanAnnotations = List.of(Controller.class, Service.class, Repository.class, Component.class);

    private final NewBeanDefinitionRegistry beanDefinitionRegistry;

    public NewClassPathBeanScanner(final NewBeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void doScan(final Object... basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        beanAnnotations.stream()
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Set::stream)
                .forEach(beanClass -> beanDefinitionRegistry.registerBeanDefinition(beanClass, SimpleBeanDefinition.from(beanClass)));
    }
}
