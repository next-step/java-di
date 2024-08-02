package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.config.BeanDefinition;
import org.junit.jupiter.api.Test;
import samples.TestConfiguration;
import samples.TestController;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationScannerTest {

    @Test
    void Configuration에_ComponentScan값을_읽어_스캔한다() {
        Map<Class<?>, BeanDefinition> actual = new ConfigurationScanner(List.of(TestConfiguration.class)).scanBean();
        assertThat(actual).containsOnlyKeys(TestConfiguration.class, TestController.class);
    }
}
