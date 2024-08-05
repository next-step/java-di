package com.interface21.beans.factory.support;


import com.interface21.beans.BeanScanFailureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.MyConfiguration;
import samples.SampleController;
import samples.SampleService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BeanScannerTest {

    @Test
    @DisplayName("특정 패키지 하위의 Bean 관련 어노테이션을 가진 클래스 정보들을 가져올 수 있다")
    void getTypesAnnotatedWithTest() {
        final BeanScanner beanScanner = new BeanScanner("samples");

        final BeanDefinitionRegistry beanDefinitionRegistry = beanScanner.scan();

        assertThat(beanDefinitionRegistry.getBeanClasses()).containsExactlyInAnyOrder(SampleController.class, SampleService.class, JdbcSampleRepository.class, MyConfiguration.class);
    }

    @Test
    @DisplayName("basePackage 를 지정하지 않으면 예외를 던진다.")
    void testEmptyBasePackage() {
        assertThatThrownBy(BeanScanner::new)
                .isInstanceOf(BeanScanFailureException.class)
                .hasMessage("basePackage can not be empty");
    }
}
