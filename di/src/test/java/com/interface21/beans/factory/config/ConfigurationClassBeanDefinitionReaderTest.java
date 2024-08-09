package com.interface21.beans.factory.config;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;
import samples.ExampleConfig;
import samples.IntegrationConfig;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationClassBeanDefinitionReaderTest {
    @Test
    public void register_simple() {
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        final ConfigurationClassBeanDefinitionReader cbr = new ConfigurationClassBeanDefinitionReader(beanFactory);
        cbr.register(ExampleConfig.class);
        beanFactory.initialize();
        beanFactory.refresh();

        assertThat(beanFactory.getBean(DataSource.class)).isNotNull();
    }

}
