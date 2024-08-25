package com.interface21.beans.factory.support;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BeanInstantiationCache {

    private final Set<Class<?>> initializingBeans = ConcurrentHashMap.newKeySet();

    public boolean isCircularDependency(Class<?> clazz) {
        return initializingBeans.contains(clazz);
    }

    public void addInitializingBean(Class<?> clazz) {
        initializingBeans.add(clazz);
    }

    public void removeInitializingBean(Class<?> clazz) {
        initializingBeans.remove(clazz);
    }
}
