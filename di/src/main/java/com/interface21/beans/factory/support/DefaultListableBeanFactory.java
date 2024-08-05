package com.interface21.beans.factory.support;

import com.interface21.beans.BeanCurrentlyInCreationException;
import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.SimpleBeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private final SimpleBeanFactory beanFactory;
    private final Set<Class<?>> tempBeansInCreation;

    public DefaultListableBeanFactory(final BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        beanFactory = new SimpleBeanFactory();
        tempBeansInCreation = new HashSet<>();
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionRegistry.getBeanClasses();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public void initialize() {
        getBeanClasses().forEach(this::initBean);
    }

    private Object initBean(final Class<?> beanClass) {
        if (tempBeansInCreation.contains(beanClass)) {
            throw new BeanCurrentlyInCreationException(tempBeansInCreation);
        }
        tempBeansInCreation.add(beanClass);

        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(beanClass, getBeanClasses())
                .orElseThrow(() -> new BeanInstantiationException(beanClass, "Could not autowire. No concrete class found for %s.".formatted(beanClass.getName())));
        final Object createdBean = createBean(beanDefinitionRegistry.getBeanDefinition(concreteClass));
        beanFactory.addBean(beanClass, createdBean);

        tempBeansInCreation.remove(beanClass);
        return createdBean;
    }

    private Object createBean(final BeanDefinition beanDefinition) {
        try {
            if (beanDefinition instanceof final SimpleBeanDefinition simpleBeanDefinition) {
                final Constructor<?> constructor = simpleBeanDefinition.getConstructor();
                return constructor.newInstance(createParameterArgs(constructor.getParameterTypes()));
            }
            throw new BeanInstantiationException(beanDefinition.getType(), "Could not instantiate bean of type '%s'".formatted(beanDefinition.getType()));
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(beanDefinition.getType(), e.getMessage(), e);
        }
    }

    private Object[] createParameterArgs(final Class<?>[] parameterTypes) {
        return Stream.of(parameterTypes)
                .map(this::getOrCreateBean)
                .toArray();
    }

    private Object getOrCreateBean(final Class<?> parameterType) {
        if (beanFactory.containsBean(parameterType)) {
            return beanFactory.getBean(parameterType);
        }
        return initBean(parameterType);
    }

    @Override
    public void clear() {
        beanDefinitionRegistry.clear();
        beanFactory.clear();
        tempBeansInCreation.clear();
    }

}
