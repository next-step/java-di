package com.interface21.beans.factory.support;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.config.AutowireStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry, AutowireCapableBeanFactory{

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final BeanDefinitions beanDefinitions = new BeanDefinitions();
    private final BeanRegistry beanRegistry;
    private final BeanInstantiationCache beanInstantiationCache;


    public DefaultListableBeanFactory() {
        this.beanRegistry = new DefaultBeanRegistry();
        this.beanInstantiationCache = new BeanInstantiationCache();
    }

    public void initialize() {
        instantiateBeans();
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitions.registerBeanDefinition(clazz, beanDefinition);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitions.getBeanClasses();
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions.getBeanDefinitions();
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitions.size();
    }

    @Override
    public BeanDefinition getBeanDefinition(Class<?> clazz) {
        return beanDefinitions.getBeanDefinition(clazz);
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return clazz.cast(doGetBean(clazz));
    }

    @Override
    public void clear() {}


    private void instantiateBeans() {
       beanDefinitions.getBeanDefinitions()
            .forEach(beanDefinition -> getBean(beanDefinition.getType()));
    }


    private Object doGetBean(Class<?> clazz) {

        Class<?> concreteClazz = resolveBeanClass(clazz);

        Object instance = Optional.ofNullable(beanRegistry.getBean(concreteClazz))
                .orElseGet(() -> createBeanInstance(beanDefinitions.getBeanDefinition(concreteClazz)));

        return concreteClazz.cast(instance);
    }


    private Object createBeanInstance(BeanDefinition beanDefinition) {

        Class<?> beanClass = beanDefinition.getType();
        if (beanInstantiationCache.isCircularDependency(beanClass)) {
            throw new BeanInstantiationException(beanClass, "Circular dependency detected");
        }
        beanInstantiationCache.addInitializingBean(beanClass);

        try {
            AutowireStrategy autowireStrategy = beanDefinition.autowireStrategy();
            return autowireStrategy.autowire(beanDefinition, this);
        } catch (IllegalArgumentException | ClassCastException e) {
            throw new BeanInstantiationException(beanClass, e.getMessage());
        } finally {
            beanInstantiationCache.removeInitializingBean(beanClass);
        }
    }

    private Class<?> resolveBeanClass(Class<?> clazz) {
        return BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses())
                .orElseThrow(() -> new BeanClassNotFoundException(clazz.getSimpleName()));
    }

    @Override
    public BeanInstantiationCache getBeanInstantiationCache() {
        return beanInstantiationCache;
    }
}
