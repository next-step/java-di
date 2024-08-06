package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import java.lang.reflect.Constructor;

public class ConstructorInjector implements Injector {
    @Override
    public Object inject(BeanDefinition beanDefinition, BeanFactory beanFactory) {
        Constructor<?> constructor = beanDefinition.getConstructor();
        Object[] dependencies = resolveDependencies(beanFactory, constructor.getParameterTypes());
        return BeanUtils.instantiateClass(constructor, dependencies);
    }
}
