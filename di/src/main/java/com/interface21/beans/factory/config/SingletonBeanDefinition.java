package com.interface21.beans.factory.config;

import com.interface21.context.annotation.Scope;

public class SingletonBeanDefinition implements BeanDefinition {

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
}
