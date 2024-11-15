package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.DefaultBeanDefinition;
import com.interface21.beans.factory.config.MethodBeanDefinitionImpl;
import com.interface21.context.annotation.Bean;
import com.interface21.context.stereotype.Component;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanScanner {

    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);
    private final Set<Class<?>> beanClasses = new HashSet<>();
    private final Set<Method> beanMethods = new HashSet<>();

    public Map<String, BeanDefinition> scan(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated, Scanners.SubTypes, Scanners.MethodsAnnotated);
        Class<?>[] needToScanAnnotation = {
                Controller.class, Service.class, Repository.class, Bean.class, Component.class
        };

        for (Class<?> annotation : needToScanAnnotation) {
            beanClasses.addAll(reflections.getTypesAnnotatedWith((Class<? extends Annotation>) annotation));
        }

        beanMethods.addAll(reflections.getMethodsAnnotatedWith(Bean.class));

        return insertBeanDefinitionMap();
    }

    private Map<String, BeanDefinition> insertBeanDefinitionMap() {
        Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
        insertBeanDefinitionFromClass(beanDefinitionMap);
        insertBeanDefinitionFromMethod(beanDefinitionMap);
        return beanDefinitionMap;
    }

    private void insertBeanDefinitionFromClass(
            Map<String, BeanDefinition> beanDefinitionMap
    ) {
        for (Class<?> clazz : beanClasses) {
            String name = decapitalize(clazz.getName());
            BeanDefinition beanDefinition = new DefaultBeanDefinition(
                    clazz.getName(),
                    clazz
            );
            beanDefinitionMap.put(name, beanDefinition);
        }
    }

    private void insertBeanDefinitionFromMethod(
            Map<String, BeanDefinition> beanDefinitionMap
    ) {
        for (Method method : beanMethods) {
            String name = decapitalize(method.getName());
            BeanDefinition beanDefinition = new MethodBeanDefinitionImpl(
                    name,
                    method.getReturnType(),
                    method
            );
            beanDefinitionMap.put(name, beanDefinition);
        }
    }

    private String decapitalize(String string) {
        if (string == null || string.isBlank()) {
            return string;
        }

        return Character.toLowerCase(string.charAt(0)) + string.substring(1);
    }
}
