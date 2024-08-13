package com.interface21.context.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.beancreator.ScannedBeanInstantiation;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import com.interface21.core.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClasspathBeanScanner implements BeanScanner{
    private static final Class<? extends Annotation>[] BEAN_CLASS_ANNOTATIONS = new Class[]{
            Repository.class,
            Service.class,
            Controller.class};

    private final BeanFactory beanFactory;

    private String[] basePackages;

    public ClasspathBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void doScan(String... initialBasePackage) {
        basePackages = initialBasePackage;

        registerBeanInstantiations();
    }

    @Override
    public void registerBeanInstantiations() {
        scanBeanClasses().forEach(clazz ->
                beanFactory.registerBeanInstantiation(clazz, new ScannedBeanInstantiation(clazz))
        );
    }

    /* visible for testing */
    String[] getBasePackages() {
        return basePackages;
    }

    /* visible for testing */
    Set<Class<?>> scanBeanClasses() {
        return ReflectionUtils.getTypesAnnotatedWith(basePackages, BEAN_CLASS_ANNOTATIONS);
    }
}
