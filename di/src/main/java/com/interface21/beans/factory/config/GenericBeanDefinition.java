package com.interface21.beans.factory.config;

import com.interface21.beans.factory.support.BeanFactoryUtils;
import java.lang.reflect.Constructor;
import java.util.Set;

public class GenericBeanDefinition implements BeanDefinition {

    private final Class<?> type;
    private final String beanClassName;
    private final Constructor<?> constructor;

    public GenericBeanDefinition(Class<?> type, String beanClassName, Constructor<?> constructor) {
        this.type = type;
        this.beanClassName = beanClassName;
        this.constructor = constructor;
    }

    public static GenericBeanDefinition from(Class<?> beanClass) {
        return new GenericBeanDefinition(beanClass, generateBeanClassName(beanClass), findBeanConstructor(beanClass));
    }

    private static String generateBeanClassName(Class<?> beanClass) {
        String simpleName = beanClass.getSimpleName();
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }

    private static Constructor<?> findBeanConstructor(Class<?> clazz) {
        Set<Constructor> injectedConstructors = BeanFactoryUtils.getInjectedConstructors(clazz);
        if (injectedConstructors.size() > 1) {
            throw new IllegalStateException("Multiple constructors annotated with @Autowired found for class: " + clazz);
        }
        if (injectedConstructors.size() == 1) {
            return injectedConstructors.iterator().next();
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 1) {
            return constructors[0];
        }

        throw new IllegalStateException("No constructor found for class: " + clazz);
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getBeanClassName() {
        return beanClassName;
    }

    @Override
    public Constructor<?> getConstructor() {
        return constructor;
    }
}
