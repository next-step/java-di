package com.interface21.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.interface21.beans.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<Class<?>, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private final BeanRegistry beanRegistry;

    public DefaultListableBeanFactory() {
        this.beanRegistry = new DefaultBeanRegistry();
    }

    public void initialize() {
        beanDefinitionMap
                .values()
                .forEach(
                        beanDefinition -> {
                            Class<?> beanClazz = beanDefinition.getType();
                            beanRegistry.registerBean(getBeanOrCreate(beanClazz));
                        });
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(clazz, beanDefinition);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.keySet();
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return List.copyOf(beanDefinitionMap.values());
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanDefinitions().size();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return (T) beanRegistry.getBean(clazz);
    }

    @Override
    public Object getBeanOrCreate(Class<?> clazz) {
        Class<?> concreteClazz =
                BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses())
                        .orElseThrow(() -> new BeanClassNotFoundException(clazz.getSimpleName()));

        if (beanRegistry.registeredBean(concreteClazz)) {
            return beanRegistry.getBean(concreteClazz);
        }
        Constructor<?> constructor = ConstructorResolver.resolveConstructor(concreteClazz);
        Object[] arguments =
                ConstructorArgumentResolver.resolveConstructorArguments(constructor, this);
        return instantiateBean(constructor, arguments);
    }

    @Override
    public void clear() {}

    private Object instantiateBean(Constructor<?> constructor, Object[] arg) {
            return BeanUtils.instantiateClass(constructor, arg);
    }
}
