package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import com.interface21.beans.factory.config.BeanDefinition;

import java.util.Arrays;

public class AutowireAnnotationBeanInitializer implements BeanInitializer {

    private final AutowireCapableBeanFactory beanFactory;
    private final BeanInstantiationCache instantiationCache;

    public AutowireAnnotationBeanInitializer(BeanFactory beanFactory) {
        this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
        this.instantiationCache = this.beanFactory.getBeanInstantiationCache();
    }

    @Override
    public boolean support(BeanDefinition beanDefinition) {

        if (beanDefinition instanceof AnnotationBeanDefinition) {
            return ((AnnotationBeanDefinition) beanDefinition).isAutowireMode();
        }
        return false;
    }

    @Override
    public Object initializeBean(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getType();

        if (instantiationCache.isCircularDependency(beanClass)) {
            throw new BeanInstantiationException(beanClass, "Circular dependency detected");
        }
        instantiationCache.addInitializingBean(beanClass);

        Object[] args = resolveArguments(beanDefinition);
        Object instance = new ConstructorResolver(beanFactory).autowireConstructor(beanDefinition, args);

        instantiationCache.removeInitializingBean(beanClass);

        return instance;
    }

    @Override
    public Object[] resolveArguments(BeanDefinition beanDefinition) {
        return Arrays.stream(beanDefinition.getParameterTypes())
                .map(beanFactory::getBean)
                .toArray(Object[]::new);
    }
}
