package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleDataSource;
import samples.JdbcSampleRepository;
import samples.SampleComponent;
import samples.SampleController;
import samples.SampleService;

class BeanScannerTest {
    @DisplayName("@Component, @Repository, @Service, @Controller 가 달린 클래스들을 스캔한다.")
    @Test
    void scan() {
        BeanScanner beanScanner = new BeanScanner("samples");

        Set<Class<?>> beanClasses = beanScanner.scan();

        assertThat(beanClasses).containsExactlyInAnyOrder(JdbcSampleDataSource.class, JdbcSampleRepository.class, SampleComponent.class, SampleController.class, SampleService.class);
    }
}