package com.interface21.beans.factory.support;

import com.interface21.beans.BeanFactoryException;
import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.BeanDefinitionMapping;
import com.interface21.beans.factory.config.ConfigurationClassBeanDefinition;
import com.interface21.beans.factory.config.RootBeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitionMapping beanDefinitionMap;

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

    private final Map<Class<?>, Set<Class<?>>> allBeanTypesBySuperType = new LinkedHashMap<>();

    public DefaultListableBeanFactory(final String... basePackages) {
        this.beanDefinitionMap = new BeanDefinitionMapping(basePackages);
        initialize();
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

    @Override
    public List<Object> getBeansForAnnotation(final Class<? extends Annotation> annotationType) {
        return singletonObjects.values().stream()
                .filter(bean -> isAnnotationOnBean(bean, annotationType))
                .toList();
    }

    public boolean isAnnotationOnBean(final Object bean, final Class<? extends Annotation> annotationType) {
        return mapToSuperTypes(bean.getClass()).stream()
                .anyMatch(clazz -> clazz.isAnnotationPresent(annotationType));
    }

    private void initialize() {
        beanDefinitionMap.scanBeanDefinitions();

        createBeansByClass(beanDefinitionMap.getBeanClasses());
    }

    private void createBeansByClass(final Set<Class<?>> preInstantiatedClasses) {
        preInstantiatedClasses.forEach(this::createOrGetBeanInstance);
    }

    private Object createOrGetBeanInstance(final Class<?> clazz) {
        if (isBeanInitialized(clazz)) {
            return getBean(clazz);
        }

        validateBeanType(clazz);

        final Class<?> beanClass = BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses()).orElse(clazz);

        final Object beanInstance = createBeanInstance(beanClass);
        saveBean(beanClass, beanInstance);

        return beanInstance;
    }

    private Object createBeanInstance(final Class<?> clazz) {
        final BeanDefinition beanDefinition = beanDefinitionMap.getBeanDefinition(clazz);

        if (beanDefinition instanceof RootBeanDefinition) {
            return createByConstructor(clazz);
        }

        return createByBeanMethod(clazz, (ConfigurationClassBeanDefinition) beanDefinition);
    }

    private boolean isBeanInitialized(final Class<?> beanType) {
        return allBeanTypesBySuperType.containsKey(beanType);
    }

    private void validateBeanType(final Class<?> clazz) {
        if (isNotBeanType(clazz)) {
            throw new BeanInstantiationException(clazz, "Class not BeanType");
        }
    }

    private Object createByConstructor(final Class<?> beanClass) {
        final Constructor<?> constructor = findConstructor(beanClass);

        try {
            final Object[] parameters = createParameters(constructor.getParameterTypes());

            constructor.setAccessible(true);

            return constructor.newInstance(parameters);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(beanClass, "Failed to instantiate bean", e);
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

    private Object createByBeanMethod(final Class<?> clazz, final ConfigurationClassBeanDefinition beanDefinition) {
        final Method factoryMethod = beanDefinition.getFactoryMethod();

        try {
            final Class<?> factoryBeanType = beanDefinition.getFactoryBeanType();

            final Object[] parameters = createParameters(factoryMethod.getParameterTypes());

            factoryMethod.setAccessible(true);

            return factoryMethod.invoke(createOrGetBeanInstance(factoryBeanType), parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException(clazz, "Failed to instantiate bean", e);
        } finally {
            factoryMethod.setAccessible(false);
        }
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

        return createOrGetBeanInstance(parameterType);
    }

    private void validateParameterType(final Class<?> parameterType) {
        if (isNotBeanType(parameterType)) {
            log.error("parameter is not bean. parameterTypes = {}", parameterType);

            throw new BeanInstantiationException(parameterType, "Parameter is not bean");
        }
    }

    private boolean isNotBeanType(final Class<?> beanType) {
        final Set<Class<?>> beanClasses = getBeanClasses();
        return !beanClasses.contains(beanType) && BeanFactoryUtils.findConcreteClass(beanType, beanClasses).isEmpty();
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
