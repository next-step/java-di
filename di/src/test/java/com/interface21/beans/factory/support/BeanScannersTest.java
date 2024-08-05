package com.interface21.beans.factory.support;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;
import samples.MyConfiguration;
import samples.SampleController;
import samples.SampleService;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannersTest {

    @Test
    @DisplayName("scan 을 통해 모든 BeanDefinitionRegistry 를 반환받을 수 있다")
    void scanTest() {
        final BeanScanners beanScanners = new BeanScanners(MyConfiguration.class);

        final BeanDefinitionRegistry beanDefinitionRegistry = beanScanners.scan();

        assertThat(beanDefinitionRegistry.getBeanClasses()).containsExactlyInAnyOrder(
                SampleController.class,
                SampleService.class,
                JdbcSampleRepository.class,
                MyConfiguration.class,
                JdbcTemplate.class,
                DataSource.class
        );
    }
}
