package com.interface21.beans.factory.config;

import com.interface21.context.annotation.Scope;

import java.lang.reflect.Method;
import java.util.List;

public class SingletonBeanDefinition extends RootBeanDefinition {

    private static final BeanScope BEAN_SCOPE = BeanScope.SINGLETON;
    private final Class<?> type;

    public SingletonBeanDefinition(Class<?> type) {
        validateScope(type);
        this.type = type;
    }

    private void validateScope(Class<?> type) {
        if (!type.isAnnotationPresent(Scope.class)) {
            return;
        }
        Scope scope = type.getAnnotation(Scope.class);
        if (scope.value() != BEAN_SCOPE) {
            throw new IllegalStateException("싱글톤이 아닌 빈으로 생성할 수 없습니다.");
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

    @Override
    public boolean isConfiguration() {
        return false;
    }

    @Override
    public List<Method> getBeanCreateMethods() {
        throw new IllegalStateException("Bean 생성 메소드를 가지지 않습니다.");
    }
}
