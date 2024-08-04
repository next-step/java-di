package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.MethodBeanDefinition;
import com.interface21.context.annotation.Configuration;
import com.interface21.context.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap;

    public DefaultBeanDefinitionRegistry(Map<String, BeanDefinition> beanDefinitionMap) {
        this.beanDefinitionMap = beanDefinitionMap;
    }

    public DefaultBeanDefinitionRegistry() {
        this(new HashMap<>());
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        if (isConfiguration(clazz)) {
            beanDefinitionMap.put(clazz.getSimpleName(), beanDefinition);
            registerMethodBeanDefinition(beanDefinition);
            return;
        }
        if (isComponentPresent(clazz)) {
            beanDefinitionMap.put(clazz.getSimpleName(), beanDefinition);
            return;
        }
        throw new IllegalArgumentException("Component가 있는 클래스만 저장할 수 있습니다.");
    }

    private void registerMethodBeanDefinition(BeanDefinition beanDefinition) {
        for (Method beanCreateMethod : beanDefinition.getBeanCreateMethods()) {
            MethodBeanDefinition methodBeanDefinition = MethodBeanDefinition.from(beanDefinition, beanCreateMethod);
            beanDefinitionMap.put(methodBeanDefinition.getBeanClassName(), methodBeanDefinition);
        }
    }

    private boolean isConfiguration(Class<?> clazz) {
        return clazz.isAnnotationPresent(Configuration.class);
    }

    private boolean isComponentPresent(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Component.class)) {
            return true;
        }
        return Arrays.stream(clazz.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(annotation -> annotation.isAnnotationPresent(Component.class));
    }

    @Override
    public BeanDefinition getBeanDefinition(Class<?> clazz) {
        return beanDefinitionMap.values()
                .stream()
                .filter(beanDefinition -> beanDefinition.isAssignableTo(clazz))
                .findAny()
                .orElseThrow(() -> new BeanInstantiationException(clazz, "생성할 수 있는 빈이 아닙니다."));
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        if (beanDefinitionMap.containsKey(beanName)) {
            return beanDefinitionMap.get(beanName);
        }
        throw new IllegalArgumentException("빈 이름에 해당하는 BeanDefinition이 없습니다.");
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitionMap.values()
                .stream()
                .toList();
    }

    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }
}
