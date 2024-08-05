package com.interface21.beans.factory.support;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConfigurationScannerTest {

    @Test
    @DisplayName("Configuration 클래스의 @ComponentScan 을 통해 basePackage 정보들을 조회할 수 있다.")
    void getBasePackagesTest() {
        final ConfigurationScanner configurationScanner = new ConfigurationScanner(List.of(ComponentScanWithDefault.class, ComponentScanWithValue.class, ComponentScanWithBasePackages.class));

        final Object[] basePackages = configurationScanner.getBasePackages();

        assertThat(basePackages).containsExactlyInAnyOrder(
                "com.interface21.beans.factory.support",
                "valueOne",
                "valueTwo",
                "baseOne",
                "baseTwo");
    }

    @Test
    @DisplayName("@Configuration 이 없는 클래스가 있으면 예외를 던진다..")
    void testEmptyBasePackage() {
        assertThatThrownBy(() -> new ConfigurationScanner(List.of(ConfigWithoutAnnotationClass.class)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Configuration 클래스의 @Bean 메서드를 찾아 BeanRegistry 에 등록한다")
    void scanBeanTest() {
        final ConfigurationScanner configurationScanner = new ConfigurationScanner(List.of(TestConfig.class));

        final BeanDefinitionRegistry beanDefinitionRegistry = configurationScanner.scanBean();

        assertThat(beanDefinitionRegistry.getBeanDefinition(TestBean.class)).isNotNull();

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
