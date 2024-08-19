package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;

class BeanScannerTest {

    private DefaultListableBeanFactory beanFactory;
    private BeanScanner beanScanner;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        beanFactory = new DefaultListableBeanFactory();
        beanScanner = new BeanScanner(beanFactory);
        beanScanner.scan("samples");
    }

    @DisplayName("@Controller, @Service, @Repository, @Component 에노테이션이 붙은 클래스를 Registry에 등록합니다.")
    @Test
    void registerBeans(){
        assertAll(
            () -> assertThat(beanFactory.getBean(JdbcSampleRepository.class)).isInstanceOf(JdbcSampleRepository.class),
            () -> assertThat(JdbcSampleRepository.class.isAnnotationPresent(Repository.class)).isTrue(),
            () -> assertThat(beanFactory.getBean(SampleController.class)).isInstanceOf(SampleController.class),
            () -> assertThat(SampleController.class.isAnnotationPresent(Controller.class)).isTrue(),
            () -> assertThat(beanFactory.getBean(SampleService.class)).isInstanceOf(SampleService.class),
            () -> assertThat(SampleService.class.isAnnotationPresent(Service.class)).isTrue()
        );

    }
}
