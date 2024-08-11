package com.interface21.beans.factory.support;

import com.interface21.context.stereotype.Component;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import org.reflections.Reflections;

public class BeanScanner {

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    private final List<Class<? extends Annotation>> beanClasses = List.of(
        Controller.class,
        Service.class,
        Repository.class,
        Component.class
    );

    public BeanScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void scan(String... basePackages) {
        Reflections reflections = new Reflections(basePackages);
        beanClasses.stream()
            .map(reflections::getTypesAnnotatedWith)
            .flatMap(Collection::stream)
            .forEach(beanClass -> beanDefinitionRegistry.registerBeanDefinition(beanClass,
                new GenericBeanDefinition(beanClass)));
    }
}
