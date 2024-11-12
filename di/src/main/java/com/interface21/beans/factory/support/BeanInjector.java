package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.BeanDefinitionImpl;
import com.interface21.beans.factory.config.MethodBeanDefinitionImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BeanInjector {
    private Map<String, BeanDefinition> beanDefinitionMap;

    public BeanInjector(Map<String, BeanDefinition> beanDefinitionMap) {
        this.beanDefinitionMap = beanDefinitionMap;
    }

    public void injectBeans(Map<Class<?>, Object> objectByClass) {
        for (BeanDefinition beanDefinition : beanDefinitionMap.values()) {
            injectBeansRecursively(objectByClass, beanDefinition, new HashSet<>());
        }
    }

    private void injectBeansRecursively(
            Map<Class<?>, Object> objectByClass,
            BeanDefinition beanDefinition,
            Set<BeanDefinition> visited
    ) {
        Class<?> targetClass = beanDefinition.getClass();
        if (objectByClass.containsKey(targetClass)) {
            return;
        }

        if (visited.contains(beanDefinition)) {
            throw new RuntimeException("recursive inject");
        }

        visited.add(beanDefinition);
        if (beanDefinition instanceof MethodBeanDefinitionImpl) {
            generateBeanFromMethod(objectByClass, beanDefinition, visited);
        } else {
            generateBeanFromConstructor(objectByClass, beanDefinition, visited);
        }
    }

    private void generateBeanFromConstructor(
            Map<Class<?>, Object> objectByClass,
            BeanDefinition beanDefinition,
            Set<BeanDefinition> visited
    ) {
        Class<?> targetClass = beanDefinition.getType();
        // TODO 빈 주입 대상이 인터페이스 인 경우등을 체크할 필요가 있음
        // TODO 일단은 생성자가 하나만 있다고 가정하는데 여러개 있는걸 다음스텝으로
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(targetClass);
        if (constructor == null) {
            try {
                objectByClass.put(
                        targetClass,
                        targetClass.getDeclaredConstructor().newInstance()
                );
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return;
        }

        objectByClass.put(
                targetClass,
                instantiateConstructor(
                        constructor,
                        objectByClass,
                        visited,
                        beanDefinition
                )
        );
    }

    private void generateBeanFromMethod(
            Map<Class<?>, Object> objectByClass,
            BeanDefinition beanDefinition,
            Set<BeanDefinition> visited
    ) {
        MethodBeanDefinitionImpl methodBeanDefinition = (MethodBeanDefinitionImpl) beanDefinition;
        Method method = methodBeanDefinition.getMethod();
        if (method.getParameters().length == 0) {
//            objectByClass.put(methodBeanDefinition.getType(), method.invoke());
        }
    }


    private Object instantiateConstructor(
            Constructor<?> constructor,
            Map<Class<?>, Object> objectByClass,
            Set<BeanDefinition> visited,
            BeanDefinition targetBeanDefinition
    ) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> args = new ArrayList();
        for (Class<?> clazz : parameterTypes) {
            Object object = getBean(clazz, objectByClass);
            if (object == null) {
                injectBeansRecursively(
                        objectByClass,
                        findBeanDefinitionByClass(clazz),
                        visited
                );
            }

            object = getBean(clazz, objectByClass);
            if (object == null) {
                throw new RuntimeException("만족하는 파라미터가 없음");
            }
            args.add(object);
        }
        return BeanUtils.instantiateClass(constructor, args.toArray());
    }

    private Object getBean(Class<?> clazz, Map<Class<?>, Object> objectByClass) {
        return objectByClass.entrySet().stream()
                .filter(v -> {
                    return clazz.isAssignableFrom(v.getKey());
                })
                .findFirst()
                .map(v -> v.getValue())
                .orElse(null);
    }

    private BeanDefinition findBeanDefinitionByClass(Class<?> clazz) {
        return beanDefinitionMap.values().stream()
                .filter(v -> {
                    return clazz.isAssignableFrom(v.getType());
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("일치하는 빈정의가 없습니다"));
    }
}
