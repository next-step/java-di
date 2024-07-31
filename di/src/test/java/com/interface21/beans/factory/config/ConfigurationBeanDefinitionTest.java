package com.interface21.beans.factory.config;

import com.interface21.context.annotation.Bean;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationBeanDefinitionTest {

    @Test
    void Configuration어노테이션이_없는_클래스로_생성하려하면_예외가_발생한다() {
        assertThatThrownBy(() -> new ConfigurationBeanDefinition(NoConfiguration.class, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Configuration 어노테이션이 달리지 않은 bean으로 생성될 수 없습니다.");
    }

    public static class NoConfiguration {
        @Bean
        public String test() {
            return "";
        }
    }
}
