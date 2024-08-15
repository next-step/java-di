package com.interface21.beans.factory.support;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Beans {
    final Map<String, Object> beans = new HashMap<>();

    public void addBean(String name, Object bean) {
        beans.put(name, bean);
    }

    public <T> T getBeanByClass(final Class<T> clazz) {
        final Object bean = beans.get(clazz.getSimpleName());
        return clazz.cast(bean);
    }

    public void clear() {
        beans.clear();
    }

    public Set<Class<?>> getBeanClasses() {
        return beans.values()
                .stream()
                .map(Object::getClass)
                .collect(Collectors.toSet());
    }

    public boolean hasBean(final String name) {
        return beans.containsKey(name);
    }

    public Map<Class<?>, Object> findBeansWithAnnotation(final Class<? extends Annotation> annotation) {
        return beans.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getClass().isAnnotationPresent(annotation))
                .collect(Collectors.toMap(entry -> entry.getValue().getClass(), Map.Entry::getValue));
    }
}
