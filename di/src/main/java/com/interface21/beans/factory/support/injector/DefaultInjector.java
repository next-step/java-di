package com.interface21.beans.factory.support.injector;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;

public class DefaultInjector implements InjectorConsumer<Object> {

    private final Class<?> clazz;

    public DefaultInjector(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean support() {
        return true;
    }

    @Override
    public Object inject(BeanFactory beanFactory) {
        return BeanUtils.instantiate(clazz);
    }
}
