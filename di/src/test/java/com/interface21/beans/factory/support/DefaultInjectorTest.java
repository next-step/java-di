package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;

@DisplayName("DefaultInjector 클래스의")
class DefaultInjectorTest {

    @DisplayName("inject 메서드는")
    @Nested
    class Inject {

        @DisplayName("BeanDefinition과 BeanFactory를 주입받아 BeanDefinition에 등록된 Constructor를 통해 Bean을 생성한다")
        @Test
        void inject() {
            // given
            final var beanFactory = new DefaultListableBeanFactory();
            final var defaultInjector = new DefaultInjector();

            // when
            final JdbcSampleRepository jdbcSampleRepository = (JdbcSampleRepository) defaultInjector.inject(new GenericBeanDefinition(JdbcSampleRepository.class), beanFactory);

            // then
            assertNotNull(jdbcSampleRepository);
        }
    }
}
