package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;

import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * 스캔된 빈이나 Configuration 안에 선언된 빈 등 여러 빈을 생성하는 인터페이스
 */
// TODO: 살펴보니 BeanDefinition 아래로 ScannedGenericBeanDefinition, ConfigurationClassBeanDefinition 이 비슷한 역할을 하고 있는 것 같다.
public interface BeanInstantiation {
    Object instantiateClass(final BeanFactory beanFactory);

    default Object[] getArgumentsFromSingletonObjects(final Parameter[] parameters, final BeanFactory beanFactory) {
        return Arrays.stream(parameters)
                     .map(parameter -> beanFactory.getBean(parameter.getType()))
                     .toArray();
    }

    Class<?> getClazz();
}
