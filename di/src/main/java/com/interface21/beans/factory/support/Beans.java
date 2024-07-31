package com.interface21.beans.factory.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Beans {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public Set<Class<?>> getBeanClasses() {
        return beans.keySet();
    }

    public boolean hasBean(Class<?> clazz) {
        return beans.containsKey(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    public void register(Object newBean) {
        beans.put(newBean.getClass(), newBean);
    }

    public void clear() {
        beans.clear();
    }
}
