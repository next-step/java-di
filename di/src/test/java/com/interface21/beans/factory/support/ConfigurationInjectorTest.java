package com.interface21.beans.factory.support;

import com.interface21.context.support.AnnotatedBeanDefinition;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.IntegrationConfig;

@DisplayName("ConfigurationInjector 클래스의")
class ConfigurationInjectorTest {

    @DisplayName("inject 메서드는")
    @Nested
    class Inject {

        private DefaultListableBeanFactory beanFactory;
        private ConfigurationInjector configurationInjector;

        @BeforeEach
        void setUp() {
            beanFactory = new DefaultListableBeanFactory();
            configurationInjector = new ConfigurationInjector();
            beanFactory.registerBeanDefinition(IntegrationConfig.class, new GenericBeanDefinition(IntegrationConfig.class));
        }

        private void registerBeanDefinitions(Class<?> configClass) {
            for (Method method : configClass.getDeclaredMethods()) {
                Class<?> returnType = method.getReturnType();
                beanFactory.registerBeanDefinition(returnType, new AnnotatedBeanDefinition(returnType, method));
            }
        }

        @DisplayName("BeanDefinition과 BeanFactory를 주입받아 BeanDefinition에 등록된 Configuration 메서드를 통해 Bean을 생성한다")
        @Test
        void testInject() {
            // given
            registerBeanDefinitions(IntegrationConfig.class);

            // when
            Method method = IntegrationConfig.class.getDeclaredMethods()[0];
            Object bean = configurationInjector.inject(new AnnotatedBeanDefinition(method.getReturnType(), method), beanFactory);

            Method method1 = IntegrationConfig.class.getDeclaredMethods()[1];
            Object bean1 = configurationInjector.inject(new AnnotatedBeanDefinition(method.getReturnType(), method), beanFactory);

            // then
            assertNotNull(bean);
            assertNotNull(bean1);
        }
    }
}
