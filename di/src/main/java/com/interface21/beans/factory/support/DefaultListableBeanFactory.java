package com.interface21.beans.factory.support;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.interface21.beans.BeanInstantiationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry, AutowireCapableBeanFactory{

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<Class<?>, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private final BeanRegistry beanRegistry;
    private final BeanInstantiationCache beanInstantiationCache;
    private final List<BeanInitializer> beanInitializers;


    public DefaultListableBeanFactory() {
        this.beanRegistry = new DefaultBeanRegistry();
        this.beanInstantiationCache = new BeanInstantiationCache();
        this.beanInitializers = List.of(new AutowireAnnotationBeanInitializer(this), new DefaultBeanInitializer(this));
    }

    public void initialize() {
        initializeBeans();
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(clazz, beanDefinition);
        log.info("BeanDefinition registered for {}", clazz.getSimpleName());
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.keySet();
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return List.copyOf(beanDefinitionMap.values());
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanDefinitions().size();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return clazz.cast(doGetBean(clazz));
    }

    @Override
    public void clear() {}


    private void initializeBeans() {
        beanDefinitionMap
            .values()
            .forEach(beanDefinition -> getBean(beanDefinition.getType()));
    }


    private Object doGetBean(Class<?> clazz) {

        Class<?> concreteClazz = resolveBeanClass(clazz);

        Object bean = beanRegistry.getBean(concreteClazz);
        if (bean != null) {
            return concreteClazz.cast(bean);
        }

        var beanDefinition = beanDefinitionMap.get(concreteClazz);
        if (beanDefinition == null) {
            throw new BeanInstantiationException(concreteClazz, "No BeanDefinition found for ");
        }
        return createBean(beanDefinition);
    }

    private Object createBean(BeanDefinition beanDefinition) {

        for (BeanInitializer beanInitializer : beanInitializers) {
            if (beanInitializer.support(beanDefinition)) {
                return beanInitializer.initializeBean(beanDefinition);
            }
        }
        return null;
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
