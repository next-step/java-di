package com.interface21.beans.factory.config;

import com.interface21.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.List;

public class ConfigurationBeanDefinition implements BeanDefinition {

    private static final BeanScope BEAN_SCOPE = BeanScope.SINGLETON;

    private final Class<?> type;
    private final List<Method> beanCreateMethods;

    public ConfigurationBeanDefinition(Class<?> type, List<Method> beanCreateMethods) {
        validateConfigurationAnnotated(type);
        this.type = type;
        this.beanCreateMethods = beanCreateMethods;
    }

    private void validateConfigurationAnnotated(Class<?> type) {
        if (!type.isAnnotationPresent(Configuration.class)) {
            throw new IllegalArgumentException("Configuration 어노테이션이 달리지 않은 bean으로 생성될 수 없습니다.");
        }
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getBeanClassName() {
        return type.getName();
    }

    @Override
    public BeanScope getScope() {
        return BEAN_SCOPE;
    }

    @Override
    public boolean isAssignableTo(Class<?> clazz) {
        return clazz.isAssignableFrom(type);
    }
}
