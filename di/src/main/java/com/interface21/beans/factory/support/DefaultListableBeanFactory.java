package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.SimpleBeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<Class<?>, BeanDefinition> beanDefinitionMap;

    private final Map<Class<?>, Object> singletonObjects;

    public DefaultListableBeanFactory() {
        final BeanScanner beanScanner = new BeanScanner("samples");
        beanDefinitionMap = beanScanner.scan()
                .stream()
                .map(SimpleBeanDefinition::new)
                .collect(Collectors.toMap(
                        SimpleBeanDefinition::getType,
                        Function.identity()
                ));
        singletonObjects = new HashMap<>();
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.keySet();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return clazz.cast(singletonObjects.get(clazz));
    }

    public void initialize() {
        getBeanClasses().forEach(this::initBean);
    }

    private Object initBean(final Class<?> beanClass) {
        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(beanClass, getBeanClasses())
                .orElseThrow(() -> new BeanInstantiationException(beanClass, "Could not autowire. No concrete class found for %s.".formatted(beanClass.getName())));
        final Object createdBean = createBean(beanDefinitionMap.get(concreteClass).getConstructor());
        singletonObjects.put(beanClass, createdBean);
        return createdBean;
    }

    private Object createBean(final Constructor<?> constructor) {
        try {
            return constructor.newInstance(createConstructorArgs(constructor));
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(constructor.getDeclaringClass(), e.getMessage(), e);
        }
    }

    private Object[] createConstructorArgs(final Constructor<?> constructor) {
        return Stream.of(constructor.getParameterTypes())
                .map(this::getOrCreateBean)
                .toArray();
    }

    private Object getOrCreateBean(final Class<?> parameterType) {
        if (singletonObjects.containsKey(parameterType)) {
            return singletonObjects.get(parameterType);
        }
        return initBean(parameterType);
    }

    @Override
    public void clear() {
        beanDefinitionMap.clear();
        singletonObjects.clear();
    }
}
