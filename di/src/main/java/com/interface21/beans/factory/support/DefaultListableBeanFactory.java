package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.CircularException;
import com.interface21.beans.factory.config.BeanDefinition;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();
    private final Set<Class<?>> circularDetection = new HashSet<>();

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.entrySet()
            .stream().map(entry -> entry.getValue().getType())
            .collect(Collectors.toSet());
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        if (singletonObjects.containsKey(clazz)) {
            return (T) singletonObjects.get(clazz);
        }

        Class<?> concreteClass = initializeAndRetrieve(clazz);

        return (T) singletonObjects.get(concreteClass);
    }

    private <T> Class<?> initializeAndRetrieve(Class<T> clazz) {
        beanInitialize(clazz, getBeanClasses());
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses())
            .orElse(clazz);
        return concreteClass;
    }

    public void initialize() {
        Set<Class<?>> definitions = getBeanClasses();
        definitions
            .forEach(
                bean -> beanInitialize(bean, definitions)
            );
    }

    private Object beanInitialize(Class<?> preInitializedBean, Set<Class<?>> definitions) {

        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(preInitializedBean, definitions)
            .orElseGet(
                () -> preInitializedBean);
        Object bean = singletonObjects.get(concreteClass.getName());

        if (bean != null) {
            return bean;
        }
        if (circularDetection.contains(preInitializedBean)) {
            throw new CircularException(circularDetection);
        }

        if (beanDefinitionMap.containsKey(concreteClass.getName())) {
            circularDetection.add(preInitializedBean);
            BeanDefinition beanDefinition = beanDefinitionMap.get(concreteClass.getName());
            bean = beanDefinition.initialize(this);
            addBeanWithClass(concreteClass, bean);
            circularDetection.remove(preInitializedBean);
            return bean;
        }

        return null;
    }

    @Override
    public void clear() {
        beanDefinitionMap.clear();
        singletonObjects.clear();
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitionMap.putIfAbsent(clazz.getName(), beanDefinition);
    }

    private void addBeanWithClass(Class<?> concreteClass, Object bean) {
        singletonObjects.putIfAbsent(concreteClass, bean);

    }
}
