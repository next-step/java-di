package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.beans.factory.config.AnnotationBeanDefinition;

import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleDataSource;
import samples.SampleService;

class DefaultListableBeanFactoryTest {

    private DefaultListableBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition(
                SampleController.class, new AnnotationBeanDefinition(SampleController.class));
        beanFactory.registerBeanDefinition(
                SampleService.class, new AnnotationBeanDefinition(SampleService.class));
        beanFactory.registerBeanDefinition(
                JdbcSampleRepository.class,
                new AnnotationBeanDefinition(JdbcSampleRepository.class));
        beanFactory.registerBeanDefinition(
                SampleDataSource.class, new AnnotationBeanDefinition(SampleDataSource.class));

        beanFactory.initialize();
    }

    @Test
    @DisplayName("BeanFactory가 초기화되면 Bean이 생성된다")
    public void initTest() {

        // then
        Set<Class<?>> beanClasses = beanFactory.getBeanClasses();
        assertThat(beanClasses)
                .contains(SampleController.class, JdbcSampleRepository.class, SampleService.class);
    }

    @Test
    public void di() {

        // when
        final var sampleController = beanFactory.getBean(SampleController.class);

        assertNotNull(sampleController);
        assertNotNull(sampleController.getSampleService());

        final var sampleService = sampleController.getSampleService();
        assertNotNull(sampleService.getSampleRepository());
    }
}
