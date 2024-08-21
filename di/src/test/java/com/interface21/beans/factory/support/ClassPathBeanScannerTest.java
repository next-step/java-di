package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinitions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.*;

import javax.sql.DataSource;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ClassPathBeanScannerTest {
    @Test
    @DisplayName("scan 을 통해 모든 BeanDefinitionRegistry 를 반환받을 수 있다")
    void scanTest() {
        final BeanScanner beanScanners = new BeanScanner(MyConfiguration.class);

        final BeanDefinitions beanDefinitions = beanScanners.scan();

        assertThat(beanDefinitions.getBeanClasses()).containsExactlyInAnyOrder(
                SampleController.class,
                SampleService.class,
                JdbcSampleRepository.class,
                MyConfiguration.class,
                JdbcTemplate.class,
                DataSource.class
        );
    }
}