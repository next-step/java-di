package com.interface21.beans.factory.support;

import java.lang.reflect.Constructor;

public final class ConstructorResolver {

    private ConstructorResolver() {}

    public static Constructor<?> resolveConstructor(Class<?> clazz) {

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor != null) {
            return injectedConstructor;
        }

        return clazz.getDeclaredConstructors()[0];
    }
}
