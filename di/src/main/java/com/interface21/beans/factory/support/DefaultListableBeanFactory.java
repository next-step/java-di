package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.exception.BeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitionRegistry registry;
    private final Beans beans;

    public DefaultListableBeanFactory(final String... basePackages) {
        this.beans = new Beans();
        this.registry = new SimpleBeanDefinitionRegistry(BeanScanner.getBeansWithAnnotation(basePackages));
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beans.getBeanClasses();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return beans.getBeanByClass(clazz);
    }

    @Override
    public void clear() {
        beans.clear();
    }

    public void initialize() {
        registry.getBeanDefinitions()
                .forEach(this::initializeBean);
    }

    public void addBean(final String name, final Object bean) {
        beans.addBean(name, bean);
    }

    private void initializeBean(final BeanDefinition beanDefinition) {
        registerBean(beanDefinition.getType());
    }

    private void registerBean(final Class<?> clazz) {
        final Object bean = instantiateClass(clazz);
        addBean(clazz.getSimpleName(), bean);
    }

    private Object instantiateClass(final Class<?> beanClass) {
        try {
            final Constructor<?> constructor = findConstructor(beanClass);
            final Object[] constructorArguments = constructorArguments(constructor);

            return BeanUtils.instantiateClass(constructor, constructorArguments);
        } catch (final Exception e) {
            throw new BeanException(e.getMessage(), e);
        }
    }


    private Constructor<?> findConstructor(final Class<?> clazz) {
        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, registry.getBeanClasses())
                .orElseThrow(() -> new BeanException("No concrete class found for: " + clazz.getName()));

        final Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);

        if (constructor == null) {
            return concreteClass.getDeclaredConstructors()[0];
        }

        return constructor;
    }

    private Object[] constructorArguments(final Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameters())
                .map(parameter -> createOrGetBean(parameter.getType()))
                .toArray();
    }

    private Object createOrGetBean(final Class<?> clazz) {
        if (!hasBean(clazz)) {
            registerBean(clazz);
        }

        return getBean(clazz);
    }

    private boolean hasBean(final Class<?> clazz) {
        return beans.hasBean(clazz.getSimpleName());
    }
}
