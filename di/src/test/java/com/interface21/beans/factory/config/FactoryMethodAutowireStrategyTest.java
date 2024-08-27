package com.interface21.beans.factory.config;

import com.interface21.beans.factory.support.ConfigurationBeanDefinition;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.config.ExampleConfig;

import javax.sql.DataSource;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class FactoryMethodAutowireStrategyTest {

    @Test
    @DisplayName("팩토리 메서드로 빈 생성을 지원한다")
    public void beanInstantiationByFactoryMethodTest() {

        Method declaredMethod = ExampleConfig.class.getDeclaredMethods()[0];
        var sut = new ConfigurationBeanDefinition(declaredMethod.getReturnType(), declaredMethod);

        var factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition(DataSource.class, sut);
        factory.registerBeanDefinition(ExampleConfig.class, new AnnotationBeanDefinition(ExampleConfig.class));

        
        var strategy = sut.autowireStrategy();
        Object instance = strategy.autowire(sut, factory);

        assertInstanceOf(DataSource.class, instance);
    }

}