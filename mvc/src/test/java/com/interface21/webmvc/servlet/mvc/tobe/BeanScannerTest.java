package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.config.BeanDefinition;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannerTest {

    @Test
    void Component연관_어노테이션을_스캔한다() {
        Map<Class<?>, BeanDefinition> actual = new BeanScanner().scanBean("samples");
        assertThat(actual).containsOnlyKeys(TestController.class);
    }
}
