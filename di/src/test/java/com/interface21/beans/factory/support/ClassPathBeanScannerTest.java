package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClassPathBeanScannerTest {

  private BeanDefinitionRegistry beanDefinitionRegistry;

  @BeforeEach
  void setUp() {
    beanDefinitionRegistry = new SimpleBeanDefinitionRegistry();
  }

  @Test
  @DisplayName("@Repository, @Service, @Controller 어노테이션이 존재하는 모든 클래스를 스캔한다")
  void scanTest() {
    List<String> basePackages = List.of("samples");
    new ClassPathBeanScanner(beanDefinitionRegistry).scan(basePackages);

    assertAll(
        () -> assertThat(beanDefinitionRegistry).isNotNull(),
        () -> assertThat(beanDefinitionRegistry.getBeanClasses()).hasSize(4)
    );
  }

}