package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;

public class DefaultInjector implements Injector {
    @Override
    public Object inject(BeanDefinition beanDefinition, BeanFactory beanFactory) {
        return BeanUtils.instantiate(beanDefinition.getType());
    }
}
