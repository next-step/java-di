package com.interface21.beans.factory.support;

import com.interface21.beans.BeanFactoryException;
import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.BeanScanner;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.beans.factory.config.BeanDefinitionMapping;
import com.interface21.context.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitionMapping beanDefinitionMap;

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

    private final Map<Class<?>, Set<Class<?>>> allBeanTypesBySuperType = new LinkedHashMap<>();

    public DefaultListableBeanFactory(final String... basePackages) {
        this.beanDefinitionMap = new BeanDefinitionMapping(basePackages);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.getBeanClasses();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        final Set<Class<?>> beanTypes = this.allBeanTypesBySuperType.get(clazz);

        if (beanTypes == null) {
            throw new BeanFactoryException("Bean not found '" + clazz + "'");
        } else if (beanTypes.size() != 1) {
            throw new BeanFactoryException("No qualifying bean of type '" + clazz
                    + "' available: expected single matching bean but found " + beanTypes.size() + ": " + beanTypes);
        }

        final Class<?> beanType = beanTypes.stream().findFirst().get();

        return (T) this.singletonObjects.get(beanType);
    }

    public void initialize() {
        beanDefinitionMap.scanBeanDefinitions();

        createBeansByClass(beanDefinitionMap.getBeanClasses());
    }

    private void createBeansByClass(final Set<Class<?>> preInstantiatedClasses) {
        preInstantiatedClasses.forEach(beanClass -> {
            if (isBeanInitialized(beanClass)) {
                return;
            }

            createBeanInstance(beanClass);
        });
    }

    private boolean isBeanInitialized(final Class<?> beanType) {
        return allBeanTypesBySuperType.containsKey(beanType);
    }

    private Object createBeanInstance(final Class<?> clazz) {
        validateBeanType(clazz);

        final Class<?> beanClass = BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses()).get();
        final Constructor<?> constructor = findConstructor(beanClass);

        try {
            final Object[] parameters = createParameters(constructor.getParameterTypes());

            constructor.setAccessible(true);

            final Object instance = constructor.newInstance(parameters);

            saveBean(beanClass, instance);

            return instance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(clazz, "Failed to instantiate bean", e);
        } finally {
            constructor.setAccessible(false);
        }
    }

    private void validateBeanType(final Class<?> clazz) {
        if (isNotBeanType(clazz)) {
            throw new BeanInstantiationException(clazz, "Class not BeanType");
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

    private Object[] createParameters(final Class<?>[] parameterTypes) {
        if (parameterTypes.length == 0) {
            return new Object[]{};
        }

        return Arrays.stream(parameterTypes)
                .map(this::getParameter).toArray();
    }

    private Object getParameter(final Class<?> parameterType) {
        validateParameterType(parameterType);

        if (isBeanInitialized(parameterType)) {
            return getBean(parameterType);
        }

        return createBeanInstance(parameterType);
    }

    private void validateParameterType(final Class<?> parameterType) {
        if (isNotBeanType(parameterType)) {
            log.error("parameter is not bean. parameterTypes = {}", parameterType);

            throw new BeanInstantiationException(parameterType, "Parameter is not bean");
        }
    }

    private boolean isNotBeanType(final Class<?> beanType) {
        return BeanFactoryUtils.findConcreteClass(beanType, getBeanClasses()).isEmpty();
    }

    private void saveBean(final Class<?> beanType, final Object bean) {
        this.singletonObjects.put(beanType, bean);

        mapToSuperTypes(bean.getClass())
                .forEach(clazz -> this.allBeanTypesBySuperType.computeIfAbsent(clazz, it -> new HashSet<>())
                        .add(beanType));
    }

    private Set<Class<?>> mapToSuperTypes(final Class<?> clazz) {
        final HashSet<Class<?>> superTypes = new HashSet<>();
        Class<?> superClass = clazz;

        while (superClass != null) {
            final Class<?>[] interfaces = superClass.getInterfaces();
            superTypes.add(superClass);
            superTypes.addAll(List.of(interfaces));

            superClass = superClass.getSuperclass();
        }

        return superTypes;
    }

    @Override
    public void clear() {
        beanDefinitionMap.clear();
        singletonObjects.clear();
    }
}
