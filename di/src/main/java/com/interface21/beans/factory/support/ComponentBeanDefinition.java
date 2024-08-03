package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import java.beans.Introspector;
import java.lang.reflect.Constructor;
import java.util.Set;

public class ComponentBeanDefinition implements BeanDefinition {
    private final Class<?> type;
    private final String name;

    public ComponentBeanDefinition(Class<?> type) {
        this.type = type;
        this.name = Introspector.decapitalize(type.getSimpleName());
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equalsType(Class<?> clazz) {
        return type == clazz;
    }

    @Override
    public boolean isImplement(Class<?> clazz) {
        Set<Class<?>> interfaces = Set.of(type.getInterfaces());
        return interfaces.contains(clazz);
    }

    public Constructor<?> getAutoWiredConstructor() {
        Constructor<?> autowiredConstructor = BeanFactoryUtils.getAutowiredConstructor(type);
        if (autowiredConstructor == null) {
            return type.getDeclaredConstructors()[0];
        }
        return autowiredConstructor;
    }
}
