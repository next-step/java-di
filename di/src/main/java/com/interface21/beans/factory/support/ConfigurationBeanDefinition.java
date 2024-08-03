package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.Set;

public class ConfigurationBeanDefinition implements BeanDefinition {
    private final Class<?> type;
    private final String name;
    private final Object configurationObject;
    private final Method creationMethod;

    public ConfigurationBeanDefinition(Class<?> type, Method creationMethod, Object configurationObject) {
        this.type = type;
        this.name = Introspector.decapitalize(creationMethod.getName());
        this.creationMethod = creationMethod;
        this.configurationObject = configurationObject;
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

    public Method getCreationMethod() {
        return creationMethod;
    }

    public Object getConfigurationObject() {
        return configurationObject;
    }
}
