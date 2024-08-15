package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.exception.BeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitionRegistry registry;
    private final Map<String, Object> singletonObjects = new HashMap<>();

    public DefaultListableBeanFactory(final String... basePackages) {
        this.registry = new SimpleBeanDefinitionRegistry(BeanScanner.getBeansWithAnnotation(basePackages));
    }

    public void addBean(final String name, final Object bean) {
        singletonObjects.put(name, bean);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.values()
                .stream()
                .map(Object::getClass)
                .collect(Collectors.toSet());
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        final Object bean = singletonObjects.get(clazz.getSimpleName());
        return clazz.cast(bean);
    }

    @Override
    public void clear() {
        singletonObjects.clear();
    }

    public void initialize() {
        registry.getBeanDefinitions()
                .forEach(this::initializeBean);
    }

    private void initializeBean(final BeanDefinition beanDefinition) {
        registerSingletonObject(beanDefinition.getType(), registry.getBeanClasses());
        final Object bean = getBean(beanDefinition.getType());
        addBean(bean.getClass().getSimpleName(), bean);
    }

    private void registerSingletonObject(final Class<?> beanClass, final Set<Class<?>> beanClasses) {
        try {
            final Constructor<?> constructor = findConstructor(beanClass, beanClasses);
            final Object[] constructorArguments = constructorArguments(beanClasses, constructor);

            final Object instance = constructor.newInstance(constructorArguments);
            singletonObjects.put(beanClass.getSimpleName(), instance);
        } catch (final Exception e) {
            throw new BeanException(e.getMessage(), e);
        }
    }


    private Constructor<?> findConstructor(final Class<?> clazz, final Set<Class<?>> beanClasses) {
        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, beanClasses)
                .orElseThrow(() -> new BeanException("No concrete class found for: " + clazz.getName()));

        final Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);

        if (constructor == null) {
            return concreteClass.getDeclaredConstructors()[0];
        }

        return constructor;
    }

    private Object[] constructorArguments(final Set<Class<?>> beanClasses, final Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameters())
                .map(parameter -> createOrGetSingletonObject(parameter.getType(), beanClasses))
                .toArray();
    }

    private Object createOrGetSingletonObject(final Class<?> clazz, final Set<Class<?>> beanClasses) {
        if (hasBean(clazz)) {
            return getBean(clazz);
        }

        registerSingletonObject(clazz, beanClasses);
        return getBean(clazz);
    }

    private boolean hasBean(final Class<?> clazz) {
        return singletonObjects.containsKey(clazz.getSimpleName());
    }
}
