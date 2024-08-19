package com.interface21.beans.factory.support.injector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class InjectorConsumerConfig {

    public static List<InjectorConsumer<?>> injectorSuppliers(
        Constructor<? extends Constructor> constructor) {

        return List.of(new ConstructorInjector(constructor));
    }

    public static InjectorConsumer<Method> injectorConfigurationSuppliers(
        Method method) {

        return new ConfigurationInjector(method);
    }
}
