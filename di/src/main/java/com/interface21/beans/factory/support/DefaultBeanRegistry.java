package com.interface21.beans.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanRegistry implements BeanRegistry {

    private final Map<Class<?>, Object> singletonObjects = new ConcurrentHashMap<>();

    public DefaultBeanRegistry() {}

    @Override
    public boolean registeredBean(Object bean) {
        var clazz = bean instanceof Class<?> ? bean : bean.getClass();

        if (bean instanceof Class<?>) {
            return singletonObjects.containsKey(bean);
        }

        singletonObjects.putIfAbsent(bean.getClass(), bean);
        return singletonObjects.containsKey(clazz);
    }

    @Override
    public Object getBean(Class<?> clazz) {
        if (!registeredBean(clazz)) {
            return null;
        }
        return singletonObjects.get(clazz);
    }
}
