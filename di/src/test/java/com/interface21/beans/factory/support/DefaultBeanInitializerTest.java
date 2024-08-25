package com.interface21.beans.factory.support;

import com.interface21.MockBeanFactory;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultBeanInitializerTest {

    private BeanFactory factory;

    @BeforeEach
    void setUp() {
        factory = MockBeanFactory.createBeanFactory();
        ((DefaultListableBeanFactory) factory).initialize();
    }

    @Test
    @DisplayName("Default 생성자가 있는 빈 초기화를 지원한다. 현재는 매개변수 없는 생성자만 지원하고있다")
    void supportTest() {

        DefaultBeanInitializer autowiredBeanInitializer = new DefaultBeanInitializer(factory);

        assertTrue(autowiredBeanInitializer.support(new AnnotationBeanDefinition(JdbcSampleRepository.class)));
    }

    @Test
    @DisplayName("빈을 생성한다")
    void initializeBeanTest() {

        DefaultBeanInitializer autowiredBeanInitializer = new DefaultBeanInitializer(factory);

        Object actual = autowiredBeanInitializer.initializeBean(new AnnotationBeanDefinition(JdbcSampleRepository.class));

        assertNotNull(actual);
        assertThat(actual).isInstanceOf(JdbcSampleRepository.class);
    }

    @Test
    @DisplayName("빈 초기화시 빈 배열의 인자를 제공한다")
    void resolveArgumentsTest() {

        DefaultBeanInitializer autowiredBeanInitializer = new DefaultBeanInitializer(factory);

        Object[] arguments = autowiredBeanInitializer.resolveArguments(new AnnotationBeanDefinition(JdbcSampleRepository.class));

        assertThat(arguments).hasSize(0);
    }

}