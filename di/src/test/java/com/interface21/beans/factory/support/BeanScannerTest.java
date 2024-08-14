package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;

class BeanScannerTest {

    @Test
    @DisplayName("BeanScanner를 통해 패키지 내의 Bean을 스캔한다")
    public void scanTest() {

        Set<Class<?>> beans = BeanScanner.scanBeans("samples");

        assertThat(beans)
                .contains(SampleController.class, SampleService.class, JdbcSampleRepository.class);
    }
}
