package com.interface21.beans.factory.config;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.BeanFactoryUtils;

import java.lang.reflect.Constructor;

public class MyScannedBeanDefinition implements BeanDefinition {
    private final Class<?> type;
    private final Constructor<?> constructor;

    public MyScannedBeanDefinition(Class<?> clazz) {
        assert !clazz.isInterface();

        this.type = clazz;
        this.constructor = BeanFactoryUtils.getInjectedConstructor(type);
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public Object instantiateClass(final BeanFactory beanFactory) {
        if (constructor == null) {
            return BeanUtils.instantiate(type);
        }

        Object[] arguments = BeanDefinition.getArgumentsFromSingletonObjects(
                constructor.getParameters(),
                beanFactory
        );
        return BeanUtils.instantiateClass(constructor, arguments);
    }
}
