package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import samples.JdbcSampleRepository;
import samples.SampleFieldService;
import samples.SampleMethodService;

@DisplayName("MethodInjector 클래스의")
class MethodInjectorTest {

    @DisplayName("inject 메서드는")
    @Nested
    class Inject {

        @DisplayName("BeanDefinition과 BeanFactory를 주입받아 BeanDefinition에 등록된 Method를 통해 Bean을 생성한다")
        void inject() {
            // given
            final var beanFactory = new DefaultListableBeanFactory();
            final var fieldInjector = new FieldInjector();

            beanFactory.registerBeanDefinition(JdbcSampleRepository.class, new GenericBeanDefinition(JdbcSampleRepository.class));
            beanFactory.registerBeanDefinition(SampleFieldService.class, new GenericBeanDefinition(SampleMethodService.class));

            // when
            final var sampleMethodService = (SampleMethodService) fieldInjector.inject(new GenericBeanDefinition(SampleMethodService.class), beanFactory);

            // then
            assertNotNull(sampleMethodService);
            assertNotNull(sampleMethodService.getSampleRepository());
        }
    }
}
