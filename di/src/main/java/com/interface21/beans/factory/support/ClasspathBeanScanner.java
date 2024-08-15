package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.MyScannedBeanDefinition;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import com.interface21.core.util.ReflectionUtils;

import java.lang.annotation.Annotation;

public class ClasspathBeanScanner {
    private static final Class<? extends Annotation>[] BEAN_CLASS_ANNOTATIONS = new Class[]{
            Repository.class,
            Service.class,
            Controller.class};

    private final BeanDefinitionRegistry beanFactory;

    private String[] basePackages;

    public ClasspathBeanScanner(BeanDefinitionRegistry beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void doScan(String... initialBasePackage) {
        basePackages = initialBasePackage;

        registerBeanDefinitions();
    }

    public void registerBeanDefinitions() {
        ReflectionUtils.getTypesAnnotatedWith(basePackages, BEAN_CLASS_ANNOTATIONS)
                       .forEach(clazz ->
                               beanFactory.registerBeanDefinition(clazz, new MyScannedBeanDefinition(clazz))
                       );
    }

    /* visible for testing */
    String[] getBasePackages() {
        return basePackages;
    }
}
