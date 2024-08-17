package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import org.junit.jupiter.api.Test;
import samples.ExampleConfig;
import samples.IntegrationConfig;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationScannerTest {

    @Test
    public void register_simple() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ConfigurationScanner cbs = new ConfigurationScanner(beanFactory);
        cbs.register(ExampleConfig.class);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }


    @Test
    public void register_classpathBeanScanner_통합() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ConfigurationScanner cbs = new ConfigurationScanner(beanFactory);
        cbs.register(IntegrationConfig.class);

        ClasspathBeanScanner cbds = new ClasspathBeanScanner(beanFactory);
        cbds.scan("samples");
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
