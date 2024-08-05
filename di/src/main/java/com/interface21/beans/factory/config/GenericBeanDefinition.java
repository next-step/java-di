package com.interface21.beans.factory.config;

public class GenericBeanDefinition implements BeanDefinition {

    private final Class<?> type;
    private final String beanClassName;

    public GenericBeanDefinition(Class<?> type, String beanClassName) {
        this.type = type;
        this.beanClassName = beanClassName;
    }

    public static GenericBeanDefinition from(Class<?> beanClass) {
        return new GenericBeanDefinition(beanClass, generateBeanClassName(beanClass));
    }

    private static String generateBeanClassName(Class<?> beanClass) {
        String simpleName = beanClass.getSimpleName();
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getBeanClassName() {
        return beanClassName;
    }
}
