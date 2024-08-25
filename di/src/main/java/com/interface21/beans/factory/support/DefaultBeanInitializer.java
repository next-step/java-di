package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import com.interface21.beans.factory.config.BeanDefinition;

public class DefaultBeanInitializer implements BeanInitializer {

    private BeanFactory beanFactory;

    public DefaultBeanInitializer(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public boolean support(BeanDefinition beanDefinition) {
        if (beanDefinition instanceof AnnotationBeanDefinition) {
            return !((AnnotationBeanDefinition) beanDefinition).isAutowireMode();
        }
        return false;
    }

    @Override
    public Object initializeBean(BeanDefinition beanDefinition) {
        Object[] args = resolveArguments(beanDefinition);
        return BeanUtils.instantiateClass(beanDefinition.getConstructor(), args);
    }

    @Override
    public Object[] resolveArguments(BeanDefinition beanDefinition) {
        return new Object[0];
    }
}
