package com.interface21.beans.factory.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationBeanDefinitionTest {

    private Method testMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        // 준비된 테스트 메서드
        testMethod = TestConfig.class.getMethod("testBean");
    }

    @Test
    @DisplayName("getType 은 메서드의 반환 타입을 반환한다.")
    void getTypeTest() {
        final ConfigurationBeanDefinition beanDefinition = ConfigurationBeanDefinition.from(testMethod);

        final Class<?> type = beanDefinition.getType();

        assertThat(type).isEqualTo(String.class);
    }

    @Test
    @DisplayName("getBeanClassName 은 메서드의 이름을 반환한다.")
    void getBeanClassNameTest() {
        final ConfigurationBeanDefinition beanDefinition = ConfigurationBeanDefinition.from(testMethod);

        final String beanClassName = beanDefinition.getBeanClassName();

        assertThat(beanClassName).isEqualTo("testBean");
    }

    @Test
    @DisplayName("getBeanMethod 는 해당 메서드를 반환한다.")
    void getBeanMethodTest() {
        final ConfigurationBeanDefinition beanDefinition = ConfigurationBeanDefinition.from(testMethod);

        final Method method = beanDefinition.getBeanMethod();

        assertThat(method).isEqualTo(testMethod);
    }

    // 테스트용 Config 클래스
    static class TestConfig {
        public String testBean() {
            return "test";
        }
    }
}
