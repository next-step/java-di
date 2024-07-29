package com.interface21.beans.factory.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultListableBeanFactoryTest {

    private Reflections reflections;
    private DefaultListableBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        reflections = new Reflections("samples");
        beanFactory = new DefaultListableBeanFactory();
        beanFactory.initialize();
    }

    @Test
    public void di() {
        final var sampleController = beanFactory.getBean(SampleController.class);

        assertNotNull(sampleController);
        assertNotNull(sampleController.getSampleService());

        final var sampleService = sampleController.getSampleService();
        assertNotNull(sampleService.getSampleRepository());
    }

    @Test
    @DisplayName("bean 들의 모든 class 정보를 가져올 수 있다.")
    void getBeanClassesTest() {
        final Set<Class<?>> beanClasses = beanFactory.getBeanClasses();

        assertThat(beanClasses).containsExactlyInAnyOrder(SampleController.class, SampleService.class, JdbcSampleRepository.class);
    }

    @Test
    @DisplayName("clear 시 모든 정보가 지워진다.")
    void clearTest() {
        beanFactory.clear();

        assertSoftly(softly -> {
            softly.assertThat(beanFactory.getBeanClasses()).isEmpty();
            softly.assertThat(beanFactory.getBean(SampleController.class)).isNull();
        });
    }
}
