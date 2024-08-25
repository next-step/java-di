package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public final class ConstructorResolver {

    public static final int FIRST_CONSTRUCTOR_INDEX = 0;

    private final BeanFactory beanFactory;

    public ConstructorResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    public static ConstructorHolder resolve(Class<?> clazz) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor != null) {
            return new ConstructorHolder(injectedConstructor, true);
        }

        return new ConstructorHolder(clazz.getDeclaredConstructors()[FIRST_CONSTRUCTOR_INDEX], false);
    }


    public Object autowireConstructor(BeanDefinition beanDefinition, Object[] args) {
        Constructor<?> constructor = beanDefinition.getConstructor();
        return BeanUtils.instantiateClass(constructor, args);
    }
}
