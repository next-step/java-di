package com.interface21.beans.factory.support;

import com.interface21.beans.BeanCurrentlyInCreationException;
import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private final SimpleBeanFactory beanFactory;
    private final Set<BeanDefinition> tempBeansInCreation;

    public DefaultListableBeanFactory(final BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        beanFactory = new SimpleBeanFactory();
        tempBeansInCreation = new HashSet<>();
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionRegistry.getBeanClasses();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public <T> T getBean(final String beanName) {
        return beanFactory.getBean(beanName);
    }

    public void initialize() {
        beanDefinitionRegistry.getBeanDefinitions()
                .forEach((s, beanDefinition) -> initBean(beanDefinition));
    }

    private Object initBean(final BeanDefinition beanDefinition) {
        if (tempBeansInCreation.contains(beanDefinition)) {
            throw new BeanCurrentlyInCreationException(tempBeansInCreation);
        }

        final String beanClassName = beanDefinition.getBeanClassName();
        if (beanFactory.containsBean(beanClassName)) {
            return beanFactory.getBean(beanClassName);
        }

        tempBeansInCreation.add(beanDefinition);

        final Object createdBean = createBean(beanDefinition);
        beanFactory.addBean(beanClassName, createdBean);

        tempBeansInCreation.remove(beanDefinition);
        return createdBean;
    }

    private Object createBean(final BeanDefinition beanDefinition) {
        try {
            return beanDefinition.createBean(this::getOrCreateBean);
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(beanDefinition.getType(), e.getMessage(), e);
        }
    }

    private Object getOrCreateBean(final Class<?> parameterType) {
        final BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(parameterType);
        final String beanClassName = beanDefinition.getBeanClassName();
        if (beanFactory.containsBean(beanClassName)) {
            return beanFactory.getBean(beanClassName);
        }
        return initBean(beanDefinition);
    }

    @Override
    public void clear() {
        beanDefinitionRegistry.clear();
        beanFactory.clear();
        tempBeansInCreation.clear();
    }

}
