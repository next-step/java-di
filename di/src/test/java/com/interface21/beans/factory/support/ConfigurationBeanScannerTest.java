package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigurationBeanScannerTest {

  private BeanDefinitionRegistry beanDefinitionRegistry;

  @BeforeEach
  void setUp() {
    beanDefinitionRegistry = new SimpleBeanDefinitionRegistry();
  }

  @Test
  @DisplayName("@Configuration 클래스의 빈 메서드들을 스캔한 뒤 반환한다")
  void beanMethodScanTest() {
    final List<String> basePackages = List.of("samples");
    new ConfigurationBeanScanner(beanDefinitionRegistry).scan(basePackages);

    assertAll(
        () -> assertThat(beanDefinitionRegistry).isNotNull(),
        () -> assertThat(beanDefinitionRegistry.getBeanDefinitions()).hasSize(2)
    );
  }

  @Test
  @DisplayName("빈 메서드의 리턴타입이 void 라면 에러를 던진다")
  void beanCreationExceptionTest() {
    final List<String> basePackages = List.of("com.interface21.beans.factory.support");
    ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(beanDefinitionRegistry);

    assertThrows(BeanCreationException.class,
        () -> configurationBeanScanner.scan(basePackages));
  }

  @Configuration
  static class ReturnVoidMethodConfiguration {

    @Bean
    public void foo() {

    }
  }
}