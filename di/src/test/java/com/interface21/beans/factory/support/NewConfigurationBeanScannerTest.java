package com.interface21.beans.factory.support;

import com.interface21.context.annotation.Bean;
import example.ExampleConfig;
import integration.IntegrationConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NewConfigurationBeanScannerTest {
    @Test
    @DisplayName("Configuration 어노테이션이 없는 클래스 등록 시 예외가 던져진다")
    void registerWithoutConfiguration() {
        final NewDefaultListableBeanFactory beanFactory = new NewDefaultListableBeanFactory();
        final NewConfigurationBeanScanner cbs = new NewConfigurationBeanScanner(beanFactory);

        assertThatThrownBy(() -> cbs.register(InvalidConfig.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static class InvalidConfig {
        @Bean
        public String test() {
            return "test";
        }
    }

    @Test
    public void register_simple() {
        final NewDefaultListableBeanFactory beanFactory = new NewDefaultListableBeanFactory();
        final NewConfigurationBeanScanner cbs = new NewConfigurationBeanScanner(beanFactory);
        cbs.register(ExampleConfig.class);
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class)).isNotNull();
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        final NewDefaultListableBeanFactory beanFactory = new NewDefaultListableBeanFactory();
        final NewConfigurationBeanScanner cbs = new NewConfigurationBeanScanner(beanFactory);
        cbs.register(IntegrationConfig.class);

        final NewClassPathBeanScanner cbds = new NewClassPathBeanScanner(beanFactory);
        cbds.doScan("samples");
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class)).isNotNull();

        final JdbcSampleRepository sampleRepository = beanFactory.getBean(JdbcSampleRepository.class);
        assertThat(sampleRepository.getDataSource()).isNotNull();

        final JdbcTemplate jdbcTemplate = beanFactory.getBean(JdbcTemplate.class);
        assertThat(jdbcTemplate.getDataSource()).isNotNull();
    }


}
