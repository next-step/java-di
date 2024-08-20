package com.interface21.beans.factory.support;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.BeanUtils;
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
                            beanRegistry.registeredBean(getBean(beanClazz));
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
        Object bean = beanRegistry.getBean(clazz);
        if (bean != null) {
            return (T) bean;
        }

        return (T) instantiateClass(clazz);
    }

    @Override
    public void clear() {}


    private Object instantiateClass(Class<?> clazz) {
        Class<?> concreteClazz =
                BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses())
                        .orElseThrow(() -> new BeanClassNotFoundException(clazz.getSimpleName()));

        var constructorResolver = new ConstructorResolver(this);
        Constructor<?> constructor = constructorResolver.resolveConstructor(concreteClazz);
        ArgumentResolver argumentResolver = constructorResolver.resolveArguments(constructor);

        return instantiateBean(constructor, argumentResolver.resolve());
    }


    private Object instantiateBean(Constructor<?> constructor, Object[] arg) {
        return BeanUtils.instantiateClass(constructor, arg);
    }
}
