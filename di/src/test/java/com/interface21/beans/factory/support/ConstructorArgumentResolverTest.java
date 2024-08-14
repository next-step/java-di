package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.SampleService;

class ConstructorArgumentResolverTest {

    private final DefaultListableBeanFactory factory = new DefaultListableBeanFactory("samples");

    @BeforeEach
    void setUp() {
        factory.initialize();
    }

    @Test
    @DisplayName("생성자의 인자를 반환한다")
    public void resolveTest() {

        Constructor<?> constructor = ConstructorResolver.resolveConstructor(SampleService.class);

        Object[] args =
                ConstructorArgumentResolver.resolveConstructorArguments(constructor, factory);

        assertThat(args).contains(factory.getBean(SampleService.class).getSampleRepository());
    }
}
