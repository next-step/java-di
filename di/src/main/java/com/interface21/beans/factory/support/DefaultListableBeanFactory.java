package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.context.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
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

    public DefaultListableBeanFactory(Map<Class<?>, BeanDefinition> beanDefinitionMap) {
        log.info("create bean factory with beanDefinitionMap: {}", beanDefinitionMap);
        this.beanDefinitionMap.putAll(beanDefinitionMap);
    }

    public void initialize() {
        log.info("initialize bean factory");
        for (Class<?> beanClass : beanDefinitionMap.keySet()) {
            getBean(beanClass);
        }
    }

    private Object initializeBean(Class<?> beanClass) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanClass);
        if (beanDefinition instanceof ConfigurationBeanDefinition) {
            return createAndStoreConfigurationBean(beanClass, beanDefinition);
        }
        Class<?> concreteClass = findConcreteClass(beanClass);
        return createAndStoreComponentBean(beanClass, beanDefinitionMap.get(concreteClass));
    }

    private Object createAndStoreConfigurationBean(Class<?> beanClass, BeanDefinition beanDefinition) {
        Method method = ((ConfigurationBeanDefinition) beanDefinition).getMethod();
        Object bean = getBean(method.getDeclaringClass());
        Object[] args = Arrays.stream(method.getParameterTypes())
                              .map(this::getBean)
                              .toArray();
        Object instance = BeanFactoryUtils.invokeMethod(method, bean, args)
                                          .orElseThrow(() -> new IllegalStateException("Failed to invoke method: " + method));
        singletonObjects.put(beanClass, instance);
        return instance;
    }

    private Class<?> findConcreteClass(Class<?> beanClass) {
        return BeanFactoryUtils.findConcreteClass(beanClass, getBeanClasses())
                               .orElseThrow(() -> new IllegalStateException("Could not find concrete class for bean class: " + beanClass));
    }

    private Object createAndStoreComponentBean(Class<?> beanClass, BeanDefinition beanDefinition) {
        Constructor<?> constructor = beanDefinition.getConstructor();
        Object[] arguments = Arrays.stream(constructor.getParameterTypes())
                                   .map(this::getBean)
                                   .toArray();
        Object instance = instantiateBean(constructor, arguments);
        singletonObjects.put(beanClass, instance);
        return instance;
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
    public Map<Class<?>, Object> getControllers() {
        return singletonObjects.keySet().stream()
                               .filter(beanClass -> beanClass.isAnnotationPresent(Controller.class))
                               .collect(Collectors.toMap(beanClass -> beanClass, this::getBean));
    }

    @Override
    public void clear() {
        singletonObjects.clear();
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(clazz, beanDefinition);
    }
}
