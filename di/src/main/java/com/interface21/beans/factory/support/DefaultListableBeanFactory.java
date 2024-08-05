package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.GenericBeanDefinition;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
        Set<Class<?>> beanClasses = beanDefinitionMap.keySet();
        for (Class<?> beanClass : beanClasses) {
            Object bean = initBean(beanClass);
            singletonObjects.put(beanClass, bean);
        }
    }

    private Object initBean(Class<?> beanClass) {
        Optional<Class<?>> concreteClass = BeanFactoryUtils.findConcreteClass(beanClass, beanDefinitionMap.keySet());
        if (concreteClass.isEmpty()) {
            throw new IllegalStateException("Could not find concrete class for bean class: " + beanClass);
        }
        return createBean(concreteClass.get());
    }

    private Object createBean(Class<?> clazz) {
        try {
            Constructor<?> constructor = findBeanConstructor(clazz);
            return constructor.newInstance(Arrays.stream(constructor.getParameterTypes())
                                                 .map(this::getOrCreateBean)
                                                 .toArray());
        } catch (Exception e) {
            log.error("Failed to create bean for class: {}", clazz, e);
            throw new IllegalStateException("Could not create bean for class: " + clazz, e);
        }
    }

    private static Constructor<?> findBeanConstructor(Class<?> clazz) {
        Set<Constructor> injectedConstructors = BeanFactoryUtils.getInjectedConstructors(clazz);
        if (injectedConstructors.size() == 1) {
            return injectedConstructors.iterator().next();
        } else if (injectedConstructors.size() > 1) {
            throw new IllegalStateException("Multiple constructors annotated with @Autowired found for class: " + clazz);
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 1) {
            return constructors[0];
        }
        throw new IllegalStateException("No constructor found for class: " + clazz);
    }

    private Object getOrCreateBean(Class<?> clazz) {
        if (singletonObjects.containsKey(clazz)) {
            return singletonObjects.get(clazz);
        }
        return initBean(clazz);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.keySet();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return (T) singletonObjects.get(clazz);
    }

    @Override
    public void clear() {
        singletonObjects.clear();
    }
}
