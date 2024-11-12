package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private Map<String, BeanDefinition> beanDefinitionMap;

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

    private final BeanScanner beanScanner = new BeanScanner();

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.keySet();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        if (!singletonObjects.containsKey(clazz)) {
            throw new RuntimeException("빈이 없습니다");
        }

        return (T) singletonObjects.get(clazz);
    }

    public void initialize(String... packages) {
        beanDefinitionMap = beanScanner.scan(packages);
        BeanInjector beanInjector = new BeanInjector(beanDefinitionMap);
        beanInjector.injectBeans(singletonObjects);
    }


    @Override
    public void clear() {
    }
}
