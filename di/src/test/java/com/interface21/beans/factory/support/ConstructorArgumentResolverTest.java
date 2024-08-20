package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.beans.factory.config.AnnotationBeanDefinition;

import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleDataSource;
import samples.SampleService;

class ConstructorArgumentResolverTest {

    private final DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

    @BeforeEach
    void setUp() {

        factory.registerBeanDefinition(
                SampleController.class, new AnnotationBeanDefinition(SampleController.class));
        factory.registerBeanDefinition(
                SampleService.class, new AnnotationBeanDefinition(SampleService.class));
        factory.registerBeanDefinition(
                JdbcSampleRepository.class,
                new AnnotationBeanDefinition(JdbcSampleRepository.class));
        factory.registerBeanDefinition(
                SampleDataSource.class, new AnnotationBeanDefinition(SampleDataSource.class));

        factory.initialize();
    }

    @Test
    @DisplayName("생성자의 인자를 반환한다")
    public void resolveTest() {

        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(SampleService.class);
        var ar = new AutowiredConstructorArgumentResolver(constructor, factory);

        assertThat(ar.resolve()).contains(factory.getBean(SampleService.class).getSampleRepository());
    }
}
