package com.interface21.beans.factory.support.injector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class InjectorConsumerConfig {

    public static List<InjectorConsumer<?>> injectorSuppliers(
            Constructor<? extends Constructor> constructor, Set<Field> fields) {

        return List.of(new ConstructorInjector(constructor));

    }

    public static List<InjectorConsumer<?>> injectorConfigurationSuppliers(
            Method method, Class<?> clazz) {

        return List.of(new ConfigurationInjector(method));

    }
}
