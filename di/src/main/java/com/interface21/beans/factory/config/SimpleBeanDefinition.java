package com.interface21.beans.factory.config;

import com.interface21.beans.BeanDefinitionException;
import com.interface21.beans.factory.support.BeanFactoryUtils;

import java.lang.reflect.Constructor;
import java.util.Set;

public class SimpleBeanDefinition implements BeanDefinition {

    private static final int BEAN_CONSTRUCTOR_COUNT = 1;
    private final Class<?> beanClass;
    private final Constructor<?> constructor;

    public SimpleBeanDefinition(final Class<?> beanClass) {
        this.beanClass = beanClass;
        this.constructor = findBeanConstructor(beanClass);
    }

    private Constructor<?> findBeanConstructor(final Class<?> concreteClass) {
        final Set<Constructor> injectedConstructors = BeanFactoryUtils.getInjectedConstructors(concreteClass);
        if (injectedConstructors.size() > BEAN_CONSTRUCTOR_COUNT) {
            throw new BeanDefinitionException(concreteClass.getName() + " - Only one constructor can have @Autowired annotation");
        }

        if (injectedConstructors.size() == BEAN_CONSTRUCTOR_COUNT) {
            return injectedConstructors.iterator().next();
        }

        final Constructor<?>[] constructors = concreteClass.getConstructors();
        if (constructors.length == BEAN_CONSTRUCTOR_COUNT) {
            return constructors[0];
        }
        throw new BeanDefinitionException(concreteClass.getName() + " - Class doesn't contain matching constructor for autowiring");
    }

    @Override
    public Class<?> getType() {
        return beanClass;
    }

    @Override
    public String getBeanClassName() {
        final String simpleName = beanClass.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    @Override
    public Constructor<?> getConstructor() {
        return constructor;
    }
}
