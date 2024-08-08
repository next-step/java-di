package com.interface21.beans.factory.config;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class RootBeanDefinition implements BeanDefinition {

    private final Class<?> type;
    private final String beanClassName;

    public RootBeanDefinition(final Class<?> type, final String beanClassName) {
        this.type = type;
        this.beanClassName = beanClassName;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }

    @Override
    public Object createInstance(final BeanGetter beanGetter) {
        return createByConstructor(beanGetter);
    }

    private Object createByConstructor(final BeanGetter beanGetter) {
        final Constructor<?> constructor = findConstructor(type);

        try {
            final Object[] parameters = Arrays.stream(constructor.getParameterTypes())
                    .map(beanGetter::getBean)
                    .toArray();

            constructor.setAccessible(true);

            return constructor.newInstance(parameters);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(type, "Failed to instantiate bean", e);
        } finally {
            constructor.setAccessible(false);
        }
    }

    private Constructor<?> findConstructor(final Class<?> clazz) {
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        return findAutowiredConstructor(constructors);
    }

    private Constructor<?> findAutowiredConstructor(final Constructor<?>[] constructors) {
        final Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .toArray(Constructor[]::new);

        if (autowiredConstructors.length != 1) {
            throw new BeanInstantiationException(constructors[0].getDeclaringClass(), "Autowire constructor not found");
        }

        return autowiredConstructors[0];
    }
}
