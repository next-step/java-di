package com.interface21.beans.factory.config;

import com.interface21.context.annotation.Scope;

import java.lang.reflect.Method;
import java.util.List;

public class MethodBeanDefinition implements BeanDefinition {

    private final String beanName;
    private final Class<?> type;
    private final BeanScope beanScope;
    private final BeanDefinition superBeanDefinition;

    public MethodBeanDefinition(String beanName, Class<?> type, BeanScope beanScope, BeanDefinition superBeanDefinition) {
        this.beanName = beanName;
        this.type = type;
        this.beanScope = beanScope;
        this.superBeanDefinition = superBeanDefinition;
    }

    public static MethodBeanDefinition from(BeanDefinition beanDefinition, Method method) {
        return new MethodBeanDefinition(
                method.getName(),
                method.getReturnType(),
                parseBeanScope(method),
                beanDefinition
        );
    }

    private static BeanScope parseBeanScope(Method method) {
        if (!method.isAnnotationPresent(Scope.class)) {
            return BeanScope.SINGLETON;
        }
        return method.getAnnotation(Scope.class)
                .value();
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getBeanClassName() {
        return beanName;
    }

    @Override
    public BeanScope getScope() {
        return beanScope;
    }

    @Override
    public boolean isAssignableTo(Class<?> clazz) {
        return clazz.isAssignableFrom(type);
    }

    @Override
    public boolean isConfiguration() {
        return false;
    }

    @Override
    public List<Method> getBeanCreateMethods() {
        throw new IllegalStateException("Bean 생성 메소드를 가지지 않습니다.");
    }

    @Override
    public boolean isSubBeanDefinition() {
        return true;
    }

    @Override
    public BeanDefinition getSuperBeanDefinition() {
        return superBeanDefinition;
    }
}
