package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.NoSuchBeanDefinitionException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.support.AnnotatedBeanDefinition;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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

        return createAndStoreBean(clazz);
    }

    @Override
    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return beanDefinitionMap.entrySet().stream()
            .filter(entry -> entry.getKey().isAnnotationPresent(annotationType))
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> getBean(entry.getKey())));
    }

    public void initialize() {
        for (Map.Entry<Class<?>, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            getBean(entry.getKey());
        }
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

    private <T> T createAndStoreBean(Class<T> beanClazz) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanClazz);

        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            return (T) createAndStoreBean(beanClazz, beanDefinition);
        }

        Class<?> concreteClazz = findConcreteClass(beanClazz);
        beanDefinition = beanDefinitionMap.get(concreteClazz);

        return (T) createAndStoreBean(concreteClazz, beanDefinition);
    }

    private Class<?> findConcreteClass(Class<?> beanClazz) {
        return BeanFactoryUtils.findConcreteClass(beanClazz, getBeanClasses())
            .orElseThrow(() -> new BeanInstantiationException(beanClazz, "Concrete class not found"));
    }

    private Object createAndStoreBean(Class<?> beanClazz, BeanDefinition beanDefinition) {
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException(beanClazz);
        }

        Object instance = inject(beanDefinition);
        singletonObjects.put(beanClazz, instance);
        return instance;
    }

    private Object inject(BeanDefinition beanDefinition) {
        InjectMode injectMode = beanDefinition.getInjectMode();
        Injector injector = injectMode.getInjector();
        return injector.inject(beanDefinition, this);
    }
}
