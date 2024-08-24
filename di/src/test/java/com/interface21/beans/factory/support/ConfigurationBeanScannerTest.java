package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.MyConfiguration;

class ConfigurationBeanScannerTest {

  @Test
  @DisplayName("@Configuration 클래스의 빈 메서드들을 스캔한 뒤 반환한다")
  void beanMethodScanTest() {
    ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(MyConfiguration.class);

    Map<Class<?>, BeanDefinition> result = configurationBeanScanner.scan();

    assertAll(
        () -> assertThat(result).isNotNull(),
        () -> assertThat(result).hasSize(2)
    );
  }

  @Test
  @DisplayName("빈 메서드의 리턴타입이 void 라면 에러를 던진다")
  void beanCreationExceptionTest() {
    ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(ReturnVoidMethodConfiguration.class);

    assertThrows(BeanCreationException.class, configurationBeanScanner::scan);
  }

  @Configuration
  @ComponentScan(basePackages = "com.interface21.beans.factory.support")
  static class ReturnVoidMethodConfiguration {

    @Bean
    public void foo() {

    }
  }
}