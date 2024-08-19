package com.interface21.beans.factory.config;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.injector.ConfigurationInjector;
import com.interface21.beans.factory.support.injector.InjectorConsumer;
import com.interface21.beans.factory.support.injector.InjectorConsumerConfig;
import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements BeanDefinition {

    private final Class<?> bean;
    private final InjectorConsumer<Method> injector;

    public ConfigurationBeanDefinition(Method method) {
        bean = method.getReturnType();
        injector = InjectorConsumerConfig.injectorConfigurationSuppliers(method);
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
        return injector;
    }


    @Override
    public Object initialize(BeanFactory beanFactory) {
        Object bean = injector.inject(beanFactory);

        return bean;
    }
}
