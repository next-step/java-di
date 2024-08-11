package com.interface21.context.support;

import com.interface21.beans.factory.support.BeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;
import com.interface21.context.annotation.ComponentScan;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final Class<?>... configurationClasses) {
        this.beanFactory = new DefaultListableBeanFactory();
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(configurationClasses);
        final var scanner = new BeanScanner(beanFactory);
        scanner.scan(getBasePackages(configurationClasses));
        beanFactory.initialize();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }

    @Override
    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return beanFactory.getBeansWithAnnotation(annotationType);
    }

    private String[] getBasePackages(Class<?>... configurationClasses) {
        return Arrays.stream(configurationClasses)
            .map(this::getBasePackages)
            .flatMap(List::stream)
            .distinct()
            .toArray(String[]::new);
    }

    private List<String> getBasePackages(Class<?> configurationClass) {
        if (!configurationClass.isAnnotationPresent(ComponentScan.class)) {
            return List.of();
        }

        ComponentScan componentScan = configurationClass.getAnnotation(ComponentScan.class);
        String[] values = componentScan.value();
        String[] basePackages = componentScan.basePackages();

        if (values.length == 0 && basePackages.length == 0) {
            return List.of(configurationClass.getPackageName());
        }

        return Stream.concat(Arrays.stream(values), Arrays.stream(basePackages))
            .toList();
    }
}
