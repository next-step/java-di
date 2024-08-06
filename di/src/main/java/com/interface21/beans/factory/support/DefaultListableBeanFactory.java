package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<Class<?>, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.keySet();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        if (singletonObjects.containsKey(clazz)) {
            return (T) singletonObjects.get(clazz);
        }
        return findAndCreateBean(clazz);
    }

    private <T> T findAndCreateBean(Class<T> clazz) {
        return BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses())
            .map(beanClazz -> (T) createAndStoreBean(beanClazz))
            .orElse(null);
    }

    private <T> T createAndStoreBean(Class<T> beanClazz) {
        Object instance = inject(beanDefinitionMap.get(beanClazz));
        singletonObjects.put(beanClazz, instance);
        return (T) instance;
    }

    public void initialize() {
        for (Map.Entry<Class<?>, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(entry.getKey(), getBeanClasses())
                .orElseThrow(() -> new BeanInstantiationException(entry.getKey(), "Concrete class not found"));
            Object bean = singletonObjects.get(concreteClazz);
            if (bean == null) {
                bean = inject(entry.getValue());
                singletonObjects.put(concreteClazz, bean);
            }
        }
    }

    private Object inject(BeanDefinition beanDefinition) {
        InjectMode injectMode = beanDefinition.getInjectMode();
        Injector injector = injectMode.getInjector();
        return injector.inject(beanDefinition, this);
    }

    @Override
    public void clear() {
        beanDefinitionMap.clear();
        singletonObjects.clear();
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(clazz, beanDefinition);
    }
}
