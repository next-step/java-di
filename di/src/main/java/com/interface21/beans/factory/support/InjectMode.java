package com.interface21.beans.factory.support;

public enum InjectMode {
    CONSTRUCTOR(new ConstructorInjector()),
    METHOD(new MethodInjector()),
    FIELD(new FieldInjector()),
    NONE(new DefaultInjector()),
    CONFIGURATION(new ConfigurationInjector());

    private final Injector injector;

    InjectMode(Injector injector) {
        this.injector = injector;
    }

    public Injector getInjector() {
        return injector;
    }
}
