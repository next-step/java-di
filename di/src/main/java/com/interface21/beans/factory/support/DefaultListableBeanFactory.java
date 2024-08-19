package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.support.injector.InjectorConsumer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

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
            .orElse(null);
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
            .orElseThrow(
                () -> new BeanInstantiationException(preInitializedBean, "No class found"));
        Object bean = singletonObjects.get(concreteClass.getName());

        if (bean != null) {
            return bean;
        }

        if (beanDefinitionMap.containsKey(concreteClass.getName())) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(concreteClass.getName());
            InjectorConsumer<?> injector = beanDefinition.getInjector();
            bean = injector.inject(this);
            addBeanWithClass(concreteClass, bean);
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
        beanDefinitionMap.put(clazz.getName(), beanDefinition);
    }

    private void addBeanWithClass(Class<?> concreteClass, Object bean) {
        singletonObjects.put(concreteClass, bean);

    }
}
