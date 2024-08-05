package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;
import samples.SampleComponent;
import samples.SampleController;
import samples.SampleService;

public class ConfigurationBeanScannerTest {
    @DisplayName("@Configuration 이 달린 클래스들을 스캔하고 @Bean이 선언된 메서드를 실행해 ConfigurationBeanDefinition을 등록한다.")
    @Test
    void scan() {
        BeanDefinitions beanDefinitions = new BeanDefinitions();
        ConfigurationBeanScanner beanScanner = new ConfigurationBeanScanner("samples");

        beanScanner.registerBeanDefinitions(beanDefinitions);

        assertAll(
                () -> assertThat(beanDefinitions.extractTypes()).containsOnly(DataSource.class, JdbcTemplate.class),
                () -> assertThat(beanDefinitions.getByType(DataSource.class)).isInstanceOf(ConfigurationBeanDefinition.class),
                () -> assertThat(beanDefinitions.extractTypes()).doesNotContain(SampleComponent.class, SampleController.class, SampleService.class, JdbcSampleRepository.class)
        );
    }
}
