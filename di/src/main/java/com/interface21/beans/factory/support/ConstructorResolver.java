package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;

public final class ConstructorResolver {

    private static final Object[] EMPTY_ARGS = new Object[0];
    public static final int FIRST_CONSTRUCTOR_INDEX = 0;
    private final BeanFactory beanFactory;


    public ConstructorResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Constructor<?> resolveConstructor(Class<?> clazz) {

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor != null) {
            return injectedConstructor;
        }

        return clazz.getDeclaredConstructors()[FIRST_CONSTRUCTOR_INDEX];
    }

    public ArgumentResolver resolveArguments(Executable executable) {
        if (executable.getParameterCount() == 0) {
            return () -> EMPTY_ARGS;
        }

        return new AutowiredConstructorArgumentResolver(executable, beanFactory);
    }
}
