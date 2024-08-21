package com.interface21.beans.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConstructorArgumentValueHolder {

    private final Map<Class<?>, ValueHolder> valueHolders = new ConcurrentHashMap<>();

    public Object[] addValueHolder(Class<?> clazz, Object[] values) {
        valueHolders.putIfAbsent(clazz, new ValueHolder(values));
        return values;
    }

    record ValueHolder(Object[] value) { }
}
