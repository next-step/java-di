package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.GenericBeanDefinition;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<Class<?>, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

    public DefaultListableBeanFactory(Set<Class<?>> beanClasses) {
        log.info("initialize bean factory with bean classes: {}", beanClasses);
        for (Class<?> beanClass : beanClasses) {
            beanDefinitionMap.put(beanClass, GenericBeanDefinition.from(beanClass));
        }
    }

    public void initialize() {
        log.info("initialize bean factory");
        for (Class<?> beanClass : beanDefinitionMap.keySet()) {
            getBean(beanClass);
        }
    }

    private Object initializeBean(Class<?> beanClass) {
        Object bean = BeanFactoryUtils.findConcreteClass(beanClass, getBeanClasses())
                                      .map(concreteClass -> createBean(beanDefinitionMap.get(concreteClass)))
                                      .orElseThrow(() -> new IllegalStateException("Could not find concrete class for bean class: " + beanClass));
        singletonObjects.put(beanClass, bean);
        return bean;
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Constructor<?> constructor = beanDefinition.getConstructor();
        Object[] arguments = Arrays.stream(constructor.getParameterTypes())
                                   .map(this::getBean)
                                   .toArray();
        return instantiateBean(constructor, arguments);
    }

    private static Object instantiateBean(Constructor<?> constructor, Object[] arguments) {
        try {
            return constructor.newInstance(arguments);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate bean", e);
        }
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.keySet();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        if (singletonObjects.containsKey(clazz)) {
            singletonObjects.get(clazz);
        }
        return (T) initializeBean(clazz);
    }

    @Override
    public void clear() {
        singletonObjects.clear();
    }
}
