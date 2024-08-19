package com.interface21.context.support;

import com.interface21.beans.factory.support.ClasspathBeanScanner;
import com.interface21.beans.factory.support.ConfigurationScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.beans.factory.support.Scanner;
import com.interface21.context.ApplicationContext;
import com.interface21.context.annotation.ComponentScan;
import java.util.Arrays;
import java.util.Set;
import org.reflections.Reflections;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final String... basePackages) {
        this.beanFactory = new DefaultListableBeanFactory();
        Scanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        Scanner configurationScanner = new ConfigurationScanner(beanFactory);
        Object[] scanPackages = getScanPackages();

        configurationScanner.scan(scanPackages);
        classpathBeanScanner.scan(scanPackages);
        classpathBeanScanner.scan(basePackages);

        beanFactory.initialize();
    }

    public static Object[] getScanPackages(final String... basePackages) {
        Reflections reflections = new Reflections(basePackages);

        return reflections.getTypesAnnotatedWith(ComponentScan.class).stream()
            .map(config -> config.getAnnotation(ComponentScan.class).value())
            .flatMap(Arrays::stream)
            .toArray();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }
}
