package com.interface21.beans.factory.support;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import example.ExampleConfig;
import integration.IntegrationConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NewConfigurationBeanScannerTest {

    @Test
    public void register_simple() {
        final NewDefaultListableBeanFactory beanFactory = new NewDefaultListableBeanFactory();
        final NewConfigurationBeanScanner cbs = new NewConfigurationBeanScanner(beanFactory);
        cbs.register(ExampleConfig.class);
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class)).isNotNull();
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        final NewDefaultListableBeanFactory beanFactory = new NewDefaultListableBeanFactory();
        final NewConfigurationBeanScanner cbs = new NewConfigurationBeanScanner(beanFactory);
        cbs.register(IntegrationConfig.class);

        final NewClassPathBeanScanner cbds = new NewClassPathBeanScanner(beanFactory);
        cbds.doScan("samples");
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class)).isNotNull();

        final JdbcSampleRepository sampleRepository = beanFactory.getBean(JdbcSampleRepository.class);
        assertThat(sampleRepository.getDataSource()).isNotNull();

        final JdbcTemplate jdbcTemplate = beanFactory.getBean(JdbcTemplate.class);
        assertThat(jdbcTemplate.getDataSource()).isNotNull();
    }
    
    @Test
    @DisplayName("Configuration 어노테이션이 없는 클래스 등록 시 예외가 던져진다")
    void registerWithoutConfiguration() {
        final NewDefaultListableBeanFactory beanFactory = new NewDefaultListableBeanFactory();
        final NewConfigurationBeanScanner cbs = new NewConfigurationBeanScanner(beanFactory);

        assertThatThrownBy(() -> cbs.register(InvalidConfig.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static class InvalidConfig {
        @Bean
        public String test() {
            return "test";
        }
    }
    
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
        public NewTestBean newTestBean() {
            return new NewTestBean();
        }

    }

    static class NewTestBean {
    }
}
