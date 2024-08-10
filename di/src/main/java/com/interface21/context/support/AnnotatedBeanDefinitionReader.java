package com.interface21.context.support;

import com.interface21.beans.factory.support.BeanDefinitionReader;
import com.interface21.beans.factory.support.BeanDefinitionRegistry;
import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.beans.factory.support.GenericBeanDefinition;
import com.interface21.context.annotation.Bean;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

public class AnnotatedBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public void loadBeanDefinitions(Class<?>... annotatedClasses) {
        registerAnnotatedClasses(annotatedClasses);
    }

    private void registerAnnotatedClasses(Class<?>... annotatedClasses) {
        Arrays.stream(annotatedClasses)
            .forEach(this::registerAnnotatedClass);
    }

    private void registerAnnotatedClass(Class<?> annotatedClass) {
        registerGenericBeanDefinition(annotatedClass);
        getBeanMethodStream(annotatedClass)
            .forEach(this::registerAnnotatedBeanDefinition);
    }

    private void registerGenericBeanDefinition(Class<?> annotatedClass) {
        beanDefinitionRegistry.registerBeanDefinition(annotatedClass, new GenericBeanDefinition(annotatedClass));
    }

    private void registerAnnotatedBeanDefinition(Method beanMethod) {
        beanDefinitionRegistry.registerBeanDefinition(beanMethod.getReturnType(),
            new AnnotatedBeanDefinition(beanMethod.getReturnType(), beanMethod));
    }

    private Stream<Method> getBeanMethodStream(Class<?> clazz) {
        return BeanFactoryUtils.getBeanMethods(clazz, Bean.class).stream();
    }
}
