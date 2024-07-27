package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private static final Random RANDOM = new Random();
    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

    @Override
    public Set<Class<?>> getBeanClasses() {
        return Set.of();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return null;
    }

    public void initialize() {
        for (BeanDefinition beanDefinition : beanDefinitionMap.values()) {
            createBean(beanDefinition);
        }
    }

    private Object createBean(BeanDefinition beanDefinition) {
        if (singletonObjects.containsKey(beanDefinition.getType())) {
            return singletonObjects.get(beanDefinition.getType());
        }
        Constructor<?> targetConstructor = getTargetConstructor(beanDefinition.getType());
        Class<?>[] parameterTypes = targetConstructor.getParameterTypes();
        if (parameterTypes.length == 0) {
            Object bean = BeanUtils.instantiateClass(targetConstructor);
            return singletonObjects.put(beanDefinition.getType(), bean);
        }

        Object[] constructorParameters = Arrays.stream(parameterTypes)
                .map(this::getBeanDefinition)
                .map(this::createBean)
                .toArray();
        Object bean = BeanUtils.instantiateClass(targetConstructor, constructorParameters);
        return singletonObjects.put(beanDefinition.getType(), bean);
    }

    private BeanDefinition getBeanDefinition(Class<?> clazz) {
        return beanDefinitionMap.values()
                .stream()
                .filter(beanDefinition -> clazz.isAssignableFrom(beanDefinition.getType()))
                .findAny()
                .orElseThrow(() -> new BeanInstantiationException(clazz, "생성할 수 있는 빈이 아닙니다."));
    }

    private Constructor<?> getTargetConstructor(Class<?> beanType) {
        Constructor<?>[] constructors = beanType.getConstructors();
        return Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findAny()
                .orElseGet(() -> randomConstructor(constructors));
    }

    private Constructor<?> randomConstructor(Constructor<?>[] constructors) {
        return constructors[RANDOM.nextInt(constructors.length)];
    }

    @Override
    public void clear() {
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        if (!isComponentPresent(clazz)) {
            throw new IllegalArgumentException("Component가 있는 클래스만 저장할 수 있습니다.");
        }
        beanDefinitionMap.put(clazz.getSimpleName(), beanDefinition);
    }

    private boolean isComponentPresent(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Component.class)) {
            return true;
        }
        return Arrays.stream(clazz.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(annotation -> annotation.isAnnotationPresent(Component.class));
    }

    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }

    public Map<Class<?>, Object> getSingletonObjects() {
        return singletonObjects;
    }
}
