package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.interface21.beans.factory.support.BeanConstructor.createTargetConstructor;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

    public DefaultListableBeanFactory() {
        this.beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
    }

    public DefaultListableBeanFactory(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
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
    public void initialize() {
        for (BeanDefinition beanDefinition : beanDefinitionRegistry.getBeanDefinitions()) {
            createBean(beanDefinition);
        }
    }

    private Object createBean(BeanDefinition beanDefinition) {
        if (isContainBean(beanDefinition.getType())) {
            return getBean(beanDefinition.getType());
        }
        return createNewBean(beanDefinition);
    }

    private boolean isContainBean(Class<?> clazz) {
        return singletonObjects.keySet()
                .stream()
                .anyMatch(clazz::isAssignableFrom);
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
        singletonObjects.put(beanDefinition.getType(), bean);
        return bean;
    }

    private Object createArgConstructorBean(BeanDefinition beanDefinition, BeanConstructor beanConstructor) {
        Object[] constructorParameters = beanConstructor.getParameterTypes()
                .stream()
                .map(beanDefinitionRegistry::getBeanDefinition)
                .map(this::createBean)
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
