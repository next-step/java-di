package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;

public enum InjectMode {
    CONSTRUCTOR(new ConstructorInjector()),
    METHOD(new MethodInjector()),
    FIELD(new FieldInjector()),
    NONE(new DefaultInjector());

    private final Injector injector;

    InjectMode(Injector injector) {
        this.injector = injector;
    }

    public Injector getInjector() {
        return injector;
    }
}
