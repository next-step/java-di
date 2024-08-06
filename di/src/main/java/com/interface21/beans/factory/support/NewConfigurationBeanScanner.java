package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.beans.factory.config.SimpleBeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Set;

public class NewConfigurationBeanScanner {
    private final NewBeanDefinitionRegistry beanDefinitionRegistry;

    public NewConfigurationBeanScanner(final NewBeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void register(final Class<?> configClass) {
        if (!configClass.isAnnotationPresent(Configuration.class)) {
            throw new IllegalArgumentException("configuration classes are not annotated with @Configuration");
        }
        beanDefinitionRegistry.registerBeanDefinition(configClass, SimpleBeanDefinition.from(configClass));
        Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> beanDefinitionRegistry.registerBeanDefinition(method.getReturnType(), ConfigurationBeanDefinition.from(method)));
    }

}
