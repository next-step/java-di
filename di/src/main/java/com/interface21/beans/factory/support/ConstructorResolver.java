package com.interface21.beans.factory.support;

import java.lang.reflect.Constructor;

public final class ConstructorResolver {

    public static final int FIRST_CONSTRUCTOR_INDEX = 0;

    private ConstructorResolver() {}

    public static Constructor<?> resolveConstructor(Class<?> clazz) {

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor != null) {
            return injectedConstructor;
        }

        return clazz.getDeclaredConstructors()[FIRST_CONSTRUCTOR_INDEX];
    }
}
