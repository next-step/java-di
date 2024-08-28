package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BeanDefinitions {

    private static final Logger log = LoggerFactory.getLogger(BeanDefinitions.class);

    private final Map<Class<?>, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitions.put(clazz, beanDefinition);
        log.info("BeanDefinition registered for {}", clazz.getSimpleName());
    }

    public BeanDefinition getBeanDefinition(Class<?> clazz) {
        BeanDefinition beanDefinition = beanDefinitions.get(clazz);
        if (beanDefinition == null) {
            throw new IllegalArgumentException("No bean definition found for " + clazz.getSimpleName());
        }
        return beanDefinition;
    }

    public List<BeanDefinition> getBeanDefinitions() {
       return List.copyOf(beanDefinitions.values());
    }

    public int size() {
       return beanDefinitions.size();
    }

    public Set<Class<?>> getBeanClasses() {
       return Collections.unmodifiableSet(beanDefinitions.keySet());
    }
}
