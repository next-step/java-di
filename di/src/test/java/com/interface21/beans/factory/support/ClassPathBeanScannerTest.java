package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.IntegrationConfig;
import samples.JdbcSampleRepository;
import samples.SampleComponent;
import samples.SampleController;
import samples.SampleService;

class ClassPathBeanScannerTest {
    @DisplayName("@Component, @Repository, @Service, @Controller 가 달린 클래스들을 스캔해서 ComponentBeanDefinition 을 등록한다.")
    @Test
    void registerComponentBeanDefinitions() {
        BeanDefinitions beanDefinitions = new BeanDefinitions();
        ClassPathBeanScanner beanScanner = new ClassPathBeanScanner("samples");

        beanScanner.registerBeanDefinitions(beanDefinitions);

        assertAll(
                () -> assertThat(beanDefinitions.extractTypes()).contains(SampleComponent.class, SampleController.class, SampleService.class, JdbcSampleRepository.class),
                () -> assertThat(beanDefinitions.getByType(SampleComponent.class)).isInstanceOf(ComponentBeanDefinition.class),
                () -> assertThat(beanDefinitions.extractTypes()).doesNotContain(IntegrationConfig.class)
        );
    }
}