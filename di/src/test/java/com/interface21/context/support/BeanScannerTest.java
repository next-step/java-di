package com.interface21.context.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.ExampleConfig;
import samples.IntegrationConfig;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;
import samples.MyConfiguration;
import samples.SampleController;
import samples.SampleService;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannerTest {
    private BeanScanner beanScanner;

    @BeforeEach
    void setUp() {
        beanScanner = new BeanScanner("samples");
        beanScanner.initialize();
    }

    @Test
    void initialize() {
        String[] basePackages = beanScanner.getBasePackages();

        assertThat(basePackages).containsExactly("samples", "camp.nextstep", "com.interface21");
    }

    @Test
    void scanBeanClasses() {
        Set<Class<?>> classes = beanScanner.scanBeanClasses();

        assertThat(classes).containsExactlyInAnyOrder(
                JdbcSampleRepository.class,
                SampleService.class,
                SampleController.class
        );
    }

    @Test
    void scanConfigurationBeans() {
        List<ConfigurationInstanceAndMethod> result = beanScanner.scanConfigurationBeans();

        assertThat(result).satisfiesExactlyInAnyOrder(
                item -> assertConfigurationInstanceAndMethod(item, ExampleConfig.class, "dataSource"),
                item -> assertConfigurationInstanceAndMethod(item, IntegrationConfig.class, "dataSource"),
                item -> assertConfigurationInstanceAndMethod(item, IntegrationConfig.class, "jdbcTemplate"),
                item -> assertConfigurationInstanceAndMethod(item, MyConfiguration.class, "dataSource"),
                item -> assertConfigurationInstanceAndMethod(item, MyConfiguration.class, "jdbcTemplate")
        );
    }

    private void assertConfigurationInstanceAndMethod(ConfigurationInstanceAndMethod record,
                                                      Class<?> expectedClass,
                                                      String methodName) {
        assertThat(record.object().getClass()).isEqualTo(expectedClass);
        assertThat(record.method().getName()).isEqualTo(methodName);
    }

    @Test
    void scanConfigurationBeanTypes() {
        Collection<Class<?>> result = beanScanner.scanConfigurationBeanTypes();
        assertThat(result).containsExactlyInAnyOrder(
                DataSource.class,
                DataSource.class,
                DataSource.class,
                JdbcTemplate.class,
                JdbcTemplate.class
        );

    }
}
