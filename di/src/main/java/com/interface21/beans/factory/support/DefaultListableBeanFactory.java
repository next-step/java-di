package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

import static com.interface21.beans.factory.support.BeanConstructor.createTargetConstructor;

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
        return (T) singletonObjects.entrySet()
                .stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 빈입니다."));
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
        return createNewBean(beanDefinition);
    }

    private Object createNewBean(BeanDefinition beanDefinition) {
        BeanConstructor targetConstructor = createTargetConstructor(beanDefinition.getType());
        if (targetConstructor.isNoArgument()) {
            return createNoArgConstructorBean(beanDefinition);
        }
        return createArgConstructorBean(beanDefinition, targetConstructor);
    }

    private Object createNoArgConstructorBean(BeanDefinition beanDefinition) {
        Object bean = BeanUtils.instantiate(beanDefinition.getType());
        return singletonObjects.put(beanDefinition.getType(), bean);
    }

    private Object createArgConstructorBean(BeanDefinition beanDefinition, BeanConstructor beanConstructor) {
        Object[] constructorParameters = beanConstructor.getParameterTypes()
                .stream()
                .map(this::getBeanDefinition)
                .map(this::createBean)
                .toArray();
        Object bean = BeanUtils.instantiateClass(beanConstructor.getConstructor(), constructorParameters);
        return singletonObjects.put(beanDefinition.getType(), bean);
    }

    private BeanDefinition getBeanDefinition(Class<?> clazz) {
        return beanDefinitionMap.values()
                .stream()
                .filter(beanDefinition -> clazz.isAssignableFrom(beanDefinition.getType()))
                .findAny()
                .orElseThrow(() -> new BeanInstantiationException(clazz, "생성할 수 있는 빈이 아닙니다."));
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
