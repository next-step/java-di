package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.IntegrationConfig;
import samples.JdbcSampleRepository;
import samples.SampleComponent;
import samples.SampleController;
import samples.SampleService;

class BeanScannerTest {
    @DisplayName("scanComponent()는 @Component, @Repository, @Service, @Controller 가 달린 클래스들을 스캔한다.")
    @Test
    void scanComponent() {
        BeanScanner beanScanner = new BeanScanner("samples");

        Set<Class<?>> beanClasses = beanScanner.scanComponent();

        assertAll(
                () -> assertThat(beanClasses).contains(SampleComponent.class, SampleController.class, SampleService.class, JdbcSampleRepository.class),
                () -> assertThat(beanClasses).doesNotContain(IntegrationConfig.class)
        );
    }

    @DisplayName("scanConfiguration()은 @Configuration 이 달린 클래스들을 스캔한다.")
    @Test
    void scanConfiguration() {
        BeanScanner beanScanner = new BeanScanner("samples");

        Set<Class<?>> beanClasses = beanScanner.scanConfiguration();

        assertAll(
                () -> assertThat(beanClasses).containsOnly(IntegrationConfig.class),
                () -> assertThat(beanClasses).doesNotContain(SampleComponent.class, SampleController.class, SampleService.class, JdbcSampleRepository.class)
        );
    }
}