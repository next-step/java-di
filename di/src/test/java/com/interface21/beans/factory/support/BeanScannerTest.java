package com.interface21.beans.factory.support;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BeanScannerTest {
  
    @DisplayName("BeanScanner component scan 테스트")
    @Test
    void getBeansWithAnnotation() {
        // given
        final String basePackages = "samples";

        // when
        final var result = BeanScanner.getBeansWithAnnotation(basePackages);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result).hasSize(5),
                () -> assertThat(result).containsAnyOf(
                        SampleController.class,
                        SampleService.class,
                        JdbcSampleRepository.class
                )
        );
    }
}
