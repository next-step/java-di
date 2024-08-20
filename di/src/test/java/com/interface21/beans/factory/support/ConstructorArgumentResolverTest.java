package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import com.interface21.MockBeanFactory;
import com.interface21.beans.factory.BeanFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import samples.JdbcSampleRepository;
import samples.SampleService;

class ConstructorArgumentResolverTest {

    private BeanFactory factory;

    @BeforeEach
    void setUp() {
        factory = MockBeanFactory.createBeanFactory();
    }

    @Test
    @DisplayName("생성자의 인자를 반환한다")
    public void resolveTest() {

        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(SampleService.class);
        var ar = new AutowiredConstructorArgumentResolver(constructor, factory);

        var typeMatched = Arrays.stream(ar.resolve())
                .anyMatch(instance -> instance instanceof JdbcSampleRepository);

        assertTrue(typeMatched);
    }
}
