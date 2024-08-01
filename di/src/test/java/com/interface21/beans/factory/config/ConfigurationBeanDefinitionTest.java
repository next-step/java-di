package com.interface21.beans.factory.config;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ConfigurationBeanDefinitionTest {

    @Test
    void Configuration어노테이션이_없는_클래스로_생성하려하면_예외가_발생한다() {
        assertThatThrownBy(() -> new ConfigurationBeanDefinition(NoConfiguration.class, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Configuration 어노테이션이 달리지 않은 bean으로 생성될 수 없습니다.");
    }

    @Test
    void bean어노테이션이_없는_메소드로_생성하려하면_예외가_발생한다() {
        List<Method> noBeanMethods = Arrays.stream(NoBeanConfiguration.class.getMethods())
                .toList();
        assertThatThrownBy(() -> new ConfigurationBeanDefinition(NoBeanConfiguration.class, noBeanMethods))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Bean 어노테이션이 없는 메소드로 생성될 수 없습니다.");
    }

    @Test
    void class만_받아_빈을_생성한다() throws NoSuchMethodException {
        Method expectedMethod = SuccessConfiguration.class.getMethod("test");
        ConfigurationBeanDefinition actual = ConfigurationBeanDefinition.from(SuccessConfiguration.class);
        assertAll(
                () -> assertThat(actual.getType()).isEqualTo(SuccessConfiguration.class),
                () -> assertThat(actual.getBeanCreateMethods()).containsExactly(expectedMethod)
        );
    }

    @Test
    void configuration인지_확인한다() {
        ConfigurationBeanDefinition beanDefinition = ConfigurationBeanDefinition.from(SuccessConfiguration.class);
        boolean actual = beanDefinition.isConfiguration();
        assertThat(actual).isTrue();
    }

    public static class NoConfiguration {
        @Bean
        public String test() {
            return "";
        }
    }

    @Configuration
    public static class NoBeanConfiguration {
        public String noBean() {
            return "";
        }
    }

    @Configuration
    public static class SuccessConfiguration {
        @Bean
        public String test() {
            return "test";
        }
    }
}
