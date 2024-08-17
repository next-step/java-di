package com.interface21.beans.factory.config;

import com.interface21.beans.factory.support.injector.InjectorConsumer;

public interface BeanDefinition {

    Class<?> getType();

    String getBeanClassName();

    InjectorConsumer<?> getInjector();
}
