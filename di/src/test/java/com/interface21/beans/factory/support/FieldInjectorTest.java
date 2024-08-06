package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleFieldService;

@DisplayName("FieldInjector 클래스의")
class FieldInjectorTest {

    @DisplayName("inject 메서드는")
    @Nested
    class Inject {

        @DisplayName("BeanDefinition과 BeanFactory를 주입받아 BeanDefinition에 등록된 Field를 통해 Bean을 생성한다")
        @Test
        void inject() {
            // given
            final var beanFactory = new DefaultListableBeanFactory();
            final var fieldInjector = new FieldInjector();

            beanFactory.registerBeanDefinition(JdbcSampleRepository.class, new GenericBeanDefinition(JdbcSampleRepository.class));
            beanFactory.registerBeanDefinition(SampleFieldService.class, new GenericBeanDefinition(SampleFieldService.class));

            // when
            final var sampleService = (SampleFieldService) fieldInjector.inject(new GenericBeanDefinition(SampleFieldService.class), beanFactory);

            // then
            assertNotNull(sampleService);
            assertNotNull(sampleService.getSampleRepository());
        }
    }
}
