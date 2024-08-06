package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;

@DisplayName("ConstructorInjector 클래스의")
class ConstructorInjectorTest {

    @DisplayName("inject 메서드는")
    @Nested
    class Inject {

        @DisplayName("BeanDefinition과 BeanFactory를 주입받아 BeanDefinition에 등록된 Constructor를 통해 Bean을 생성한다")
        @Test
        void inject() {
            // given
            final var beanFactory = new DefaultListableBeanFactory();
            final var constructorInjector = new ConstructorInjector();

            beanFactory.registerBeanDefinition(JdbcSampleRepository.class, new GenericBeanDefinition(JdbcSampleRepository.class));
            beanFactory.registerBeanDefinition(SampleService.class, new GenericBeanDefinition(SampleService.class));
            beanFactory.registerBeanDefinition(SampleController.class, new GenericBeanDefinition(SampleController.class));

            // when
            final SampleController sampleController = (SampleController) constructorInjector.inject(new GenericBeanDefinition(SampleController.class), beanFactory);

            // then
            assertNotNull(sampleController);
            assertNotNull(sampleController.getSampleService());
            assertNotNull(sampleController.getSampleService().getSampleRepository());
        }
    }
}
