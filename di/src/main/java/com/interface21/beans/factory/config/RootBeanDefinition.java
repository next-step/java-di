package com.interface21.beans.factory.config;

public abstract class RootBeanDefinition implements BeanDefinition {

    @Override
    public final boolean isSubBeanDefinition() {
        return false;
    }

    @Override
    public final BeanDefinition getSuperBeanDefinition() {
        throw new IllegalStateException("SuperBeanDefinition이 없어 반환할 수 없습니다.");
    }
}
