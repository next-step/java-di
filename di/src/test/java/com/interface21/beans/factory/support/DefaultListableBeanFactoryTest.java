package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import com.interface21.MockBeanFactory;
import com.interface21.beans.factory.BeanFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.beans.factory.config.AnnotationBeanDefinition;

import org.mockito.Mock;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleDataSource;
import samples.SampleService;

class DefaultListableBeanFactoryTest {

    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        this.beanFactory = MockBeanFactory.createBeanFactory();
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


    @Test
    @DisplayName("조회한 빈이 없으면 생성해서 반환한다")
    public void getBeanTest() {

        beanFactory = MockBeanFactory.createBeanFactory();

        SampleController bean = beanFactory.getBean(SampleController.class);

        assertNotNull(bean);
    }
}
