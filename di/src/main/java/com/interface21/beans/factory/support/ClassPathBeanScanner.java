package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinitions;
import com.interface21.beans.factory.config.SimpleBeanDefinition;
import com.interface21.context.annotation.Configuration;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public class ClassPathBeanScanner implements BeanDefinitionScanner {
    private final List<Class<? extends Annotation>> beanAnnotations;
    private final Reflections reflections;

    public ClassPathBeanScanner(final Object... basePackage) {
        if (basePackage == null || basePackage.length == 0) {
            throw new IllegalArgumentException("basePackage can not be empty");
        }
        beanAnnotations = List.of(Controller.class, Service.class, Repository.class, Configuration.class);
        reflections = new Reflections(basePackage);
    }

    @Override
    public BeanDefinitions scan() {
        final BeanDefinitions beanDefinitions = new BeanDefinitions();
        beanAnnotations.stream()
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Set::stream)
                .forEach(beanClass -> beanDefinitions.registerBeanDefinition(beanClass, SimpleBeanDefinition.from(beanClass)));
        return beanDefinitions;
    }
}

