package com.interface21.context.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;

class BeanScannerTest {

    @Test
    @DisplayName("타겟이 되는 어노테이션이 붙은 클래스를 스캔한다.")
    void scan() {
        // given
        BeanScanner beanScanner = new BeanScanner("samples");

        // when
        Set<Class<?>> result = beanScanner.scan();

        //then
        assertAll(
            () -> assertThat(result).contains(SampleController.class),
            () -> assertThat(result).contains(SampleService.class),
            () -> assertThat(result).contains(JdbcSampleRepository.class)
        );
    }
}
