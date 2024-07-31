package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import java.beans.Introspector;

public class DefaultBeanDefinition implements BeanDefinition {
    private final Class<?> type;
    private final String name;

    public DefaultBeanDefinition(Class<?> type) {
        this.type = type;
        this.name = Introspector.decapitalize(type.getSimpleName());
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getBeanClassName() {
        return name;
    }
}
