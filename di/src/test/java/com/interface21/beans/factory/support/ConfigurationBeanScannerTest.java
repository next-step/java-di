package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.webmvc.servlet.mvc.tobe.BeanScanner;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.ExampleConfig;
import samples.IntegrationConfig;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;
import samples.SampleComponent;
import samples.SampleController;
import samples.SampleService;

public class ConfigurationBeanScannerTest {


/*    @Test
    public void register_simple() {
        BeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanScanner cbs = new BeanScanner(beanFactory);
        cbs.register(ExampleConfig.class);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        BeanFactory beanFactory = new DefaultListableBeanFactory();
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
    }*/
}
