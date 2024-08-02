package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleRepository;
import samples.SampleService;

class BeanDefinitionsTest {
    @DisplayName("등록된 BeanDefinitions의 type을 추출한다.")
    @Test
    void extractTypes() {
        BeanDefinitions beanDefinitions = new BeanDefinitions();
        beanDefinitions.registerBeanDefinitions(Set.of(SampleService.class));

        Set<Class<?>> beanTypes = beanDefinitions.extractTypes();

        assertThat(beanTypes).containsOnly(SampleService.class);
    }

    @DisplayName("넘겨받은 Class와 타입이 같거나 인터페이스를 구현하는 클래스가 있을 경우 등록되었다고 취급한다.")
    @Test
    void isRegistered() {
        BeanDefinitions beanDefinitions = new BeanDefinitions();
        beanDefinitions.registerBeanDefinitions(Set.of(SampleService.class, JdbcSampleRepository.class));

        assertAll(
                () -> assertThat(beanDefinitions.isRegistered(SampleService.class)).isTrue(),
                () -> assertThat(beanDefinitions.isRegistered(SampleRepository.class)).isTrue()
        );
    }
}