package com.interface21.beans.factory.support;

import com.interface21.beans.BeanFactoryException;
import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.BeanDefinitionMapping;
import com.interface21.beans.factory.config.ConfigurationClassBeanDefinitionReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitionMapping beanDefinitionMap;

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

    private final Map<Class<?>, Set<Class<?>>> allBeanTypesBySuperType = new LinkedHashMap<>();

    public DefaultListableBeanFactory(final String... basePackages) {
        this.beanDefinitionMap = new BeanDefinitionMapping(basePackages);
        beanDefinitionMap.scanBeanDefinitions();
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

    public void refresh() {
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

        return createBeanInstance(beanClass);
    }

    private Object createBeanInstance(final Class<?> clazz) {
        final BeanDefinition beanDefinition = beanDefinitionMap.getBeanDefinition(clazz);

        final Object beanInstance = beanDefinition.createInstance(this::createOrGetBeanInstance);

        saveBean(clazz, beanInstance);

        return beanInstance;
    }

    private boolean isBeanInitialized(final Class<?> beanType) {
        return allBeanTypesBySuperType.containsKey(beanType);
    }

    private void validateBeanType(final Class<?> clazz) {
        if (isNotBeanType(clazz)) {
            throw new BeanInstantiationException(clazz, "Class not BeanType");
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

    @Override
    public void registerBeanDefinition(final Class<?> clazz, final BeanDefinition beanDefinition) {
        beanDefinitionMap.registerBeanDefinition(clazz, beanDefinition);
    }
}
