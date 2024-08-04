package com.interface21.beans.factory.config;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SubBeanDefinitionTest {

    private final BeanDefinition beanDefinition = ConfigurationBeanDefinition.from(SuccessConfiguration.class);
    private final Method method = SuccessConfiguration.class.getMethod("test");
    private final SubBeanDefinition subBeanDefinition = SubBeanDefinition.from(beanDefinition, method);

    SubBeanDefinitionTest() throws NoSuchMethodException {
    }

    @Test
    void Method로_BeanDefinition을_생성한다() {
        SubBeanDefinition actual = SubBeanDefinition.from(beanDefinition, method);
        assertAll(
                () -> assertThat(actual.getType()).isEqualTo(String.class),
                () -> assertThat(actual.getBeanClassName()).isEqualTo("test")
        );
    }

    @Test
    void configuration가_아닌지_확인한다() {
        boolean actual = subBeanDefinition.isConfiguration();
        assertThat(actual).isFalse();
    }

    @Test
    void createMethod를_반환하려하면_예외가_발생한다() {
        assertThatThrownBy(subBeanDefinition::getBeanCreateMethods)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Bean 생성 메소드를 가지지 않습니다.");
    }

    @Test
    void subBeanDefinition인지_확인한다() {
        boolean actual = subBeanDefinition.isSubBeanDefinition();
        assertThat(actual).isTrue();
    }

    @Test
    void 상위_BeanDefinition을_반환한다() {
        BeanDefinition actual = subBeanDefinition.getSuperBeanDefinition();
        assertThat(actual).isEqualTo(beanDefinition);
    }

    @Configuration
    public static class SuccessConfiguration {
        @Bean
        public String test() {
            return "test";
        }
    }
}
