package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinition;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanDefinitions {
    private final Map<String, BeanDefinition> nameToBeanDefinitionMap = new HashMap<>();

    public void register(BeanDefinition beanDefinition) {
        nameToBeanDefinitionMap.put(beanDefinition.getName(), beanDefinition);
    }

    public Set<Class<?>> extractTypes() {
        return nameToBeanDefinitionMap.values()
                .stream()
                .map(BeanDefinition::getType)
                .collect(Collectors.toSet());
    }

    public BeanDefinition getByType(Class<?> clazz) {
        return nameToBeanDefinitionMap.values()
                .stream()
                .filter(beanDefinition -> beanDefinition.equalsType(clazz))
                .findAny()
                .or(() -> findConcreteTypeBeanDefinition(clazz))
                .orElseThrow(() -> new IllegalArgumentException("해당하는 타입의 빈이 없습니다. type=%s".formatted(clazz.getSimpleName())));
    }

    private Optional<BeanDefinition> findConcreteTypeBeanDefinition(Class<?> clazz) {
        checkMultipleConcreteClasses(clazz);
        return nameToBeanDefinitionMap.values()
                .stream()
                .filter(beanDefinition -> beanDefinition.isImplement(clazz))
                .findAny();
    }

    private void checkMultipleConcreteClasses(Class<?> clazz) {
        long count = nameToBeanDefinitionMap.values()
                .stream()
                .filter(beanDefinition -> beanDefinition.isImplement(clazz))
                .count();

        if (count > 1) {
            throw new IllegalArgumentException("해당 인터페이스를 지원하는 구체 클래스가 여러개입니다. (interface=%s)".formatted(clazz.getSimpleName()));
        }
    }
}
