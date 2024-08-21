package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.AnnotationBeanDefinition;

import java.lang.reflect.Constructor;

public final class ConstructorResolver {

    private static final Object[] EMPTY_ARGS = new Object[0];
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


    public Object autowireConstructor(AnnotationBeanDefinition beanDefinition) {

        Class<?>[] parameterTypes = beanDefinition.getParameterTypes();
        var args = this.beanFactory.registerArgumentValues(beanDefinition.getType(), parameterTypes);

        if (parameterTypes.length != args.length) {
            throw new IllegalStateException("Failed to resolve arguments for constructor");
        }

        return beanFactory.getBean(beanDefinition.getType());
    }
}
