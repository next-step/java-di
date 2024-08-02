package com.interface21.beans.factory.support;

import java.util.HashSet;
import java.util.Set;

public class CircularReferenceSensor {
    private final Set<Class<?>> beanBeingCreated = new HashSet<>();

    public void detect(Class<?> type) {
        if (beanBeingCreated.contains(type)) {
            throw new CircularReferenceException();
        }
    }

    public void addTarget(Class<?> clazz) {
        beanBeingCreated.add(clazz);
    }

    public void removeAllTargets() {
        beanBeingCreated.clear();
    }
}
