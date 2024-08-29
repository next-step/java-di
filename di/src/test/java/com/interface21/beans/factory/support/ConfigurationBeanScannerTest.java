package com.interface21.beans.factory.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;
import samples.config.ExampleConfig;
import samples.config.IntegrationConfig;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest {

    private BeanDefinitionRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new DefaultListableBeanFactory();
    }

    @Test
    @DisplayName("@Configuration 클래스에 선언된 @Bean 을 스캔한다")
    public void scanTest() {

        var scanner = new ConfigurationBeanScanner(registry);

        int scanned = scanner.scan(new String[]{"samples.config"});

        assertThat(scanned).isEqualTo(4);
    }


    @Test
    @DisplayName("@Configuration이 없으면 스캔 결과는 0을 리턴한다")
    public void scanZeroTest() {

        var scanner = new ConfigurationBeanScanner(registry);

        int scanned = scanner.scan(new String[]{"circular"});

        assertThat(scanned).isEqualTo(0);
    }

    @Test
    public void register_simple() {

        var beanFactory = new DefaultListableBeanFactory();
        var cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.scan(BeanScanner.scanBasePackages(ExampleConfig.class));
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void register_classpathBeanScanner_통합() {

        var beanFactory = new DefaultListableBeanFactory();
        var cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.scan(BeanScanner.scanBasePackages(IntegrationConfig.class));

        ClasspathBeanScanner cbds = new ClasspathBeanScanner(beanFactory);
        cbds.scan(new String[]{"samples"});
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));

        JdbcSampleRepository sampleRepository = beanFactory.getBean(JdbcSampleRepository.class);
        assertNotNull(sampleRepository);
        assertNotNull(sampleRepository.getDataSource());

        JdbcTemplate jdbcTemplate = beanFactory.getBean(JdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }
}