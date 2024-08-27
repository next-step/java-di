package com.interface21.beans.factory.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationBeanDefinitionScannerTest {

    private BeanDefinitionRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new DefaultListableBeanFactory();
    }

    @Test
    @DisplayName("@Configuration을 스캔한다")
    public void scanTest() {

        var scanner = new ConfigurationBeanDefinitionScanner(registry);

        int scanned = scanner.scan(new String[]{"samples.config"});

        assertThat(scanned).isEqualTo(4);
    }


    @Test
    @DisplayName("@Configuration이 없으면 스캔 결과는 0을 리턴한다")
    public void scanZeroTest() {

        var scanner = new ConfigurationBeanDefinitionScanner(registry);

        int scanned = scanner.scan(new String[]{"circular"});

        assertThat(scanned).isEqualTo(0);
    }

}