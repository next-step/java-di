package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.interface21.beans.factory.support.BeanConstructor.createTargetConstructor;
import static java.util.stream.Collectors.toMap;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

    public DefaultListableBeanFactory(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public DefaultListableBeanFactory() {
        this(new DefaultBeanDefinitionRegistry());
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.keySet();
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

    @Override
    public Map<Class<?>, Object> getControllers() {
        return singletonObjects.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(Controller.class))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void initialize() {
        for (BeanDefinition beanDefinition : beanDefinitionRegistry.getBeanDefinitions()) {
            createBean(beanDefinition, new HashSet<>());
        }
    }

    private Object createBean(BeanDefinition beanDefinition, Set<BeanDefinition> preBeanDefinitions) {
        validateAndSetPreBeanDefinitions(beanDefinition, preBeanDefinitions);
        if (isContainBean(beanDefinition.getType())) {
            return getBean(beanDefinition.getType());
        }
        return createNewBean(beanDefinition, preBeanDefinitions);
    }

    private static void validateAndSetPreBeanDefinitions(BeanDefinition beanDefinition, Set<BeanDefinition> preBeanDefinitions) {
        if (preBeanDefinitions.contains(beanDefinition)) {
            throw new IllegalStateException("순환참조인 빈이 있어 초기화할 수 없습니다.");
        }
        preBeanDefinitions.add(beanDefinition);
    }

    private boolean isContainBean(Class<?> clazz) {
        return singletonObjects.keySet()
                .stream()
                .anyMatch(clazz::isAssignableFrom);
    }

    private Object createNewBean(BeanDefinition beanDefinition, Set<BeanDefinition> preBeanDefinition) {
        BeanConstructor targetConstructor = createTargetConstructor(beanDefinition.getType());
        if (targetConstructor.isNoArgument()) {
            return createNoArgConstructorBean(beanDefinition);
        }
        return createArgConstructorBean(beanDefinition, targetConstructor, preBeanDefinition);
    }

    private Object createNoArgConstructorBean(BeanDefinition beanDefinition) {
        Object bean = BeanUtils.instantiate(beanDefinition.getType());
        singletonObjects.put(beanDefinition.getType(), bean);
        return bean;
    }

    private Object createArgConstructorBean(
            BeanDefinition beanDefinition,
            BeanConstructor beanConstructor,
            Set<BeanDefinition> preBeanDefinition
    ) {
        Object[] constructorParameters = beanConstructor.getParameterTypes()
                .stream()
                .map(beanDefinitionRegistry::getBeanDefinition)
                .map(subBeanDefinition -> createBean(subBeanDefinition, preBeanDefinition))
                .toArray();
        Object bean = BeanUtils.instantiateClass(beanConstructor.getConstructor(), constructorParameters);
        return singletonObjects.put(beanDefinition.getType(), bean);
    }

    @Override
    public void clear() {
    }

    public Map<Class<?>, Object> getSingletonObjects() {
        return singletonObjects;
    }
}
