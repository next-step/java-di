package com.interface21.beans.factory.support;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.ExampleConfig;
import samples.IntegrationConfig;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest {
    @Test
    @DisplayName("클래스를 인자로 register 가 잘 동작한다")
    public void register_simple() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(ExampleConfig.class);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(IntegrationConfig.class);

        ClasspathBeanScanner cbds = new ClasspathBeanScanner(beanFactory);
        cbds.doScan("samples");
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));

        JdbcSampleRepository sampleRepository = beanFactory.getBean(JdbcSampleRepository.class);
        assertNotNull(sampleRepository);
        assertNotNull(sampleRepository.getDataSource());

        JdbcTemplate jdbcTemplate = beanFactory.getBean(JdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());

        String[] basePackages = cbds.getBasePackages();
        assertThat(basePackages).containsExactly("samples");
    }

}
