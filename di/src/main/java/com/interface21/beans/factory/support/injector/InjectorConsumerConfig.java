package com.interface21.beans.factory.support.injector;

import com.interface21.context.annotation.Configuration;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@Configuration
public class InjectorConsumerConfig {

    public static List<InjectorConsumer<?>> injectorSuppliers(
        Constructor<? extends Constructor> constructor) {

        return List.of(new ConstructorInjector(constructor));

    }
}
