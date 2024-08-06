package com.interface21.beans.factory.support;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleComponent;
import samples.SampleController;
import samples.SampleService;

import static org.assertj.core.api.Assertions.assertThat;

class NewClassPathBeanScannerTest {

    @Test
    @DisplayName("scan 을 통해 모든 BeanDefinitionRegistry 에 특정 패키지 하위 클래스들 정보를 등록할 수 있다")
    void scanTest() {
        final NewDefaultListableBeanFactory beanDefinitionRegistry = new NewDefaultListableBeanFactory();
        final NewClassPathBeanScanner beanScanners = new NewClassPathBeanScanner(beanDefinitionRegistry);

        beanScanners.doScan("samples");

        assertThat(beanDefinitionRegistry.getBeanClasses()).containsExactlyInAnyOrder(
                SampleController.class,
                SampleService.class,
                JdbcSampleRepository.class,
                SampleComponent.class
        );
    }
}
