package com.interface21.beans.factory.config;

public interface BeanDefinition {

    interface BeanGetter {
        Object getBean(final Class<?> clazz);
    }

    Class<?> getType();

    String getBeanClassName();

    Object createInstance(final BeanGetter beanGetter);
}
