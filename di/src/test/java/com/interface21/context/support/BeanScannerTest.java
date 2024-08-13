package com.interface21.context.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.ExampleConfig;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;
import samples.SampleController;
import samples.SampleRepository;
import samples.SampleService;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BeanScannerTest {
    @Test
    @DisplayName("클래스를 인자로 register 가 잘 동작한다")
    public void register_simple() {
        BeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanScanner cbs = new BeanScanner(beanFactory);
        cbs.register(ExampleConfig.class);
        beanFactory.initialize();

        assertThat(cbs.getBasePackages()).containsExactly("samples", "camp.nextstep", "com.interface21");
        assertNotNull(beanFactory.getBean(JdbcTemplate.class));
        assertNotNull(beanFactory.getBean(DataSource.class));
        assertNotNull(beanFactory.getBean(SampleService.class));
        assertNotNull(beanFactory.getBean(SampleController.class));
        assertNotNull(beanFactory.getBean(SampleRepository.class));
        assertNotNull(beanFactory.getBean(JdbcSampleRepository.class));
        assertEquals(beanFactory.getBean(SampleRepository.class), beanFactory.getBean(JdbcSampleRepository.class));
    }

}
