package com.interface21.beans.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanRegistry implements BeanRegistry {

    private final Map<Class<?>, Object> singletonObjects = new ConcurrentHashMap<>();

    public DefaultBeanRegistry() {
    }

    @Override
    public void registerBean(Object bean) {

        if (registeredBean(bean)) {
            throw new IllegalStateException("Bean already registered [%s]".formatted(bean.getClass().getSimpleName()));
        }

        singletonObjects.put(bean.getClass(), bean);
    }

    @Override
    public boolean registeredBean(Object bean) {
        return singletonObjects.containsKey(bean.getClass());
    }

    @Override
    public boolean registeredBean(Class<?> clazz) {
        return singletonObjects.containsKey(clazz);
    }

    @Override
    public Object getBean(Class<?> clazz) {
        if (!registeredBean(clazz)) {
            throw new BeanClassNotFoundException("Bean not registered [%s]".formatted(clazz.getSimpleName()));
        }
        return singletonObjects.get(clazz);
    }
}
