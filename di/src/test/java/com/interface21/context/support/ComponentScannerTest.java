package com.interface21.context.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.beans.factory.config.BeanDefinition;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;
import samples.TestConfig;

class ComponentScannerTest {

    @Test
    @DisplayName("타겟이 되는 어노테이션이 붙은 클래스를 스캔한다.")
    void scan() {
        // given
        ComponentScanner componentScanner = ComponentScanner.from(TestConfig.class);

        // when
        Map<Class<?>, BeanDefinition> result = componentScanner.scan();

        //then
        assertAll(
            () -> assertThat(result.keySet()).contains(SampleController.class),
            () -> assertThat(result.keySet()).contains(SampleService.class),
            () -> assertThat(result.keySet()).contains(JdbcSampleRepository.class)
        );
    }
}
