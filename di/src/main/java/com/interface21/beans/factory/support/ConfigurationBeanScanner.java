package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ConfigurationBeanScanner {
    private static final String STEREOTYPE_PACKAGE = "com.interface21.context.stereotype";

    private final Reflections reflections;

    public ConfigurationBeanScanner(String... basePackages) {
        this.reflections = new Reflections(STEREOTYPE_PACKAGE, basePackages);
    }

    public Set<Class<?>> scan() {
        return new HashSet<>(reflections.getTypesAnnotatedWith(Configuration.class));
    }

    public void registerBeanDefinitions(BeanDefinitions beanDefinitions) {
        Set<Class<?>> configurationClasses = scan();
        configurationClasses.forEach(configurationClass -> {
            Object configurationObject = BeanUtils.instantiate(configurationClass);
            BeanFactoryUtils.getBeanMethods(configurationClass, Bean.class)
                    .forEach(beanMethod -> {
                        Class<?> beanClass = beanMethod.getReturnType();
                        beanDefinitions.register(new ConfigurationBeanDefinition(beanClass, beanMethod, configurationObject));
                    });
        });
    }
}
