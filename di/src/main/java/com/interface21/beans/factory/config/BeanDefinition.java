package com.interface21.beans.factory.config;

import com.interface21.beans.factory.BeanFactory;

import java.lang.reflect.Parameter;
import java.util.Arrays;

public interface BeanDefinition {

    Class<?> getType();

    String getBeanClassName();

    // 별도로 생성 책임을 담당하는 애가 생길때까지 BeanDefinition 이 객체 초기화를 담당
    Object instantiateClass(BeanFactory beanFactory);

    static Object[] getArgumentsFromSingletonObjects(final Parameter[] parameters, final BeanFactory beanFactory) {
        return Arrays.stream(parameters)
                     .map(parameter -> beanFactory.getBean(parameter.getType()))
                     .toArray();
    }

}
