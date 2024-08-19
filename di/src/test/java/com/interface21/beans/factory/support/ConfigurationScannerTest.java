package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.interface21.context.support.AnnotationConfigWebApplicationContext;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;

class ConfigurationScannerTest {

    public JdbcTemplate jdbcTemplate;
    public JdbcSampleRepository jdbcSampleRepository;
    public DataSource dataSource;

    @Test
    public void register_simple() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ConfigurationScanner configurationScanner = new ConfigurationScanner(beanFactory);
        Object[] scanPackages = AnnotationConfigWebApplicationContext.getScanPackages("samples");

        configurationScanner.scan(scanPackages);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }


    @Test
    public void register_classpathBeanScanner_통합() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ConfigurationScanner configurationScanner = new ConfigurationScanner(beanFactory);
        Object[] scanPackages = AnnotationConfigWebApplicationContext.getScanPackages("samples");
        configurationScanner.scan(scanPackages);

        ClasspathBeanScanner cbds = new ClasspathBeanScanner(beanFactory);
        cbds.scan("samples");
        beanFactory.initialize();

        dataSource = beanFactory.getBean(DataSource.class);
        jdbcSampleRepository = beanFactory.getBean(JdbcSampleRepository.class);
        jdbcTemplate = beanFactory.getBean(JdbcTemplate.class);
        assertNotNull(jdbcSampleRepository);
        assertNotNull(jdbcSampleRepository.getDataSource());
        assertNotNull(jdbcTemplate);
        assertThat(jdbcTemplate.getDataSource()).isEqualTo(jdbcSampleRepository.getDataSource());
    }
}
