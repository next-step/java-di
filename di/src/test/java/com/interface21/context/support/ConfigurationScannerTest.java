package com.interface21.context.support;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;
import samples.TestConfig;

class ConfigurationScannerTest {

    @Test
    public void register_simple() {
        ConfigurationScanner configurationScanner = new ConfigurationScanner(TestConfig.class);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory(configurationScanner.scan());
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        ComponentScanner componentScanner = ComponentScanner.from(TestConfig.class);
        ConfigurationScanner configurationScanner = new ConfigurationScanner(TestConfig.class);
        Map<Class<?>, BeanDefinition> beanDefinitions = componentScanner.scan();
        beanDefinitions.putAll(configurationScanner.scan());
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory(beanDefinitions);
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
