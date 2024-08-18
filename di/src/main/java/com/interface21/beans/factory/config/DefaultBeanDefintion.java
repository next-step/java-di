package com.interface21.beans.factory.config;

import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.beans.factory.support.injector.DefaultInjector;
import com.interface21.beans.factory.support.injector.InjectorConsumer;
import com.interface21.beans.factory.support.injector.InjectorConsumerConfig;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class DefaultBeanDefintion implements BeanDefinition {

    private final Class<?> bean;
    private final List<InjectorConsumer<?>> injectors;
    private final InjectorConsumer<Object> defaultInjector;

    public DefaultBeanDefintion(Class<?> beanClazz) {
        this.bean = beanClazz;
        Constructor<? extends Constructor> constructor = BeanFactoryUtils.getInjectedConstructor(
            beanClazz);
        this.injectors = InjectorConsumerConfig.injectorSuppliers(constructor);
        this.defaultInjector = new DefaultInjector(bean);
    }

    @Override
    public Class<?> getType() {
        return bean;
    }

    @Override
    public String getBeanClassName() {
        return bean.getName();
    }

    @Override
    public InjectorConsumer<?> getInjector() {
        return injectors.stream()
            .filter(InjectorConsumer::support)
            .findFirst()
            .orElse(defaultInjector);
    }

}
