package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.ConstructorInjector;
import com.interface21.beans.factory.config.FactoryMethodInjector;
import com.interface21.beans.factory.config.Injector;

public enum InjectionType {

    CONSTRUCTOR(new ConstructorInjector()),
    METHOD(new FactoryMethodInjector()),
    ;

    private final Injector injector;

    InjectionType(Injector injector) {
        this.injector = injector;
    }

    public Injector getInjector() {
        return injector;
    }

}
