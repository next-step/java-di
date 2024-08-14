package com.interface21.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class BeansCache {
    private final Map<Class<?>, Object> beanClasses;

    public BeansCache() {
        this(new HashMap<>());
    }

    private BeansCache(Map<Class<?>, Object> beanClasses) {
        this.beanClasses = beanClasses;
    }

    public <T> Object get(Class<T> clazz) {
        return beanClasses.get(clazz);
    }

    public void store(Class<?> clazz, Object instance) {
        beanClasses.put(clazz, instance);
    }

    public void forEach(BiConsumer<Class<?>, Object> biConsumer) {
        beanClasses.forEach(biConsumer);
    }

    public BeansCache filter(Function<Class<?>, Boolean> tester) {
        return new BeansCache(
                beanClasses.keySet().stream()
                           .filter(tester::apply)
                           .collect(toMap(identity(), beanClasses::get)));
    }

    public Set<Class<?>> getAllBeanClasses() {
        return beanClasses.keySet();
    }
}
