package com.interface21.beans.factory.config;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.support.BeanFactoryUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class SimpleBeanDefinition implements BeanDefinition {

    private static final int BEAN_CONSTRUCTOR_COUNT = 1;

    private final Class<?> beanClass;
    private final Constructor<?> constructor;

    public SimpleBeanDefinition(final Class<?> beanClass, final Constructor<?> constructor) {
        this.beanClass = beanClass;
        this.constructor = constructor;
    }

    public static SimpleBeanDefinition from(final Class<?> beanClass) {
        return new SimpleBeanDefinition(beanClass, findBeanConstructor(beanClass));
    }

    private static Constructor<?> findBeanConstructor(Class<?> concreteClass) {
        Set<Constructor> injectedConstructors = BeanFactoryUtils.getInjectedConstructors(concreteClass);

        if (injectedConstructors.size() > BEAN_CONSTRUCTOR_COUNT) {
            throw new BeanInstantiationException(concreteClass, " - Only one constructor can have @Autowired annotation");
        }

        if (injectedConstructors.size() == BEAN_CONSTRUCTOR_COUNT) {
            return injectedConstructors.iterator().next();
        }

        final Constructor<?>[] constructors = concreteClass.getConstructors();
        if (constructors.length == BEAN_CONSTRUCTOR_COUNT) {
            return constructors[0];
        }

        throw new BeanInstantiationException(concreteClass, " - Class doesn't contain matching constructor for autowiring");
    }

    @Override
    public Class<?> getType() {
        return beanClass;
    }

    @Override
    public String getBeanClassName() {
        return beanClass.getSimpleName();
    }

    @Override
    public Object createBean(final Function<Class<?>, Object> beanSupplier) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        return constructor.newInstance(createParameterArgs(beanSupplier));
    }

    private Object[] createParameterArgs(final Function<Class<?>, Object> beanSupplier) {
        return Stream.of(constructor.getParameterTypes())
                .map(beanSupplier)
                .toArray();
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }
}
