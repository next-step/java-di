package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;

@DisplayName("BeanScanner 클래스의")
class BeanScannerTest {

    @DisplayName("scan 메서드는")
    @Nested
    class Scan {

        @DisplayName("basePackages에 지정한 패키지 경로를 스캔하여 Controller, Service, Repository 어노테이션이 붙은 클래스를 BeanDefinitionRegistry에 등록한다")
        @Test
        void registerBeanDefinition() {
            // given
            BeanDefinitionRegistry beanDefinitionRegistry = new DefaultListableBeanFactory();
            BeanScanner beanScanner = new BeanScanner(beanDefinitionRegistry);

            // when
            beanScanner.scan("samples");
            // then
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanDefinitionRegistry;
            assertNotNull(defaultListableBeanFactory.getBean(SampleController.class));
            assertNotNull(defaultListableBeanFactory.getBean(JdbcSampleRepository.class));
            assertNotNull(defaultListableBeanFactory.getBean(SampleService.class));
        }
    }
}
