package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinitions;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConfigurationBeanScannerTest {
    @Test
    @DisplayName("Configuration 클래스의 @ComponentScan 을 통해 basePackage 정보들을 조회할 수 있다.")
    void getBasePackagesTest() {
        final ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(List.of(ComponentScanWithDefault.class, ComponentScanWithValue.class, ComponentScanWithBasePackages.class, ComponentScanWithComplexValues.class));

        final Object[] basePackages = configurationBeanScanner.getBasePackages();

        assertThat(basePackages).containsExactlyInAnyOrder(
                "com.interface21.beans.factory.support",
                "valueOne",
                "valueTwo",
                "baseOne",
                "baseTwo",
                "complexValue",
                "complexBaseOne",
                "complexBaseTwo"
        );
    }

    @Test
    @DisplayName("@Configuration 이 없는 클래스가 있으면 예외를 던진다.")
    void testEmptyBasePackage() {
        assertThatThrownBy(() -> new ConfigurationBeanScanner(List.of(ConfigWithoutAnnotationClass.class)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Configuration 클래스의 @Bean 메서드를 찾는다")
    void scanTest() {
        final ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(List.of(TestConfig.class));

        BeanDefinitions beanDefinitions = configurationBeanScanner.scan();

        assertThat(beanDefinitions.getBeanClasses()).contains(TestBean.class);
    }

    @Configuration
    @ComponentScan
    public static class ComponentScanWithDefault {

    }

    @Configuration
    @ComponentScan({"valueOne", "valueTwo"})
    public static class ComponentScanWithValue {

    }

    @Configuration
    @ComponentScan(basePackages = {"baseOne", "baseTwo"})
    public static class ComponentScanWithBasePackages {

    }

    @Configuration
    @ComponentScan(value = {"complexValue"}, basePackages = {"complexBaseOne", "complexBaseTwo"})
    public static class ComponentScanWithComplexValues {

    }

    public static class ConfigWithoutAnnotationClass {

    }

    @Configuration
    @ComponentScan
    static class TestConfig {
        @Bean
        public TestBean testBean() {
            return new TestBean();
        }

    }

    static class TestBean {
    }
}