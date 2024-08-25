package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.AnnotationBeanDefinition;

public class AnnotationBeanDefinitionScanner implements BeanDefinitionScanner {

    private final BeanDefinitionRegistry registry;

    public AnnotationBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public int scan(String[] basePackages) {
        BeanScanner.scanBeans(basePackages).forEach(this::registerBeanDefinition);

        return this.registry.getBeanDefinitionCount();
    }

    private void registerBeanDefinition(Class<?> beanClass) {
        this.registry.registerBeanDefinition(beanClass, new AnnotationBeanDefinition(beanClass));
    }
}
