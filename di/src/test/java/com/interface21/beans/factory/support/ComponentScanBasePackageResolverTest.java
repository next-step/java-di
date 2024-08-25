package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.context.annotation.ComponentScan;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ComponentScanBasePackageResolverTest {

  @Test
  @DisplayName("@ComponentScan 애노테이션을 통해 basePackages 를 조회한다")
  void testGetBasePackages() {
    final List<String> result = ComponentScanBasePackageResolver.getBasePackages(TestConfiguration.class);

    assertAll(
        () -> assertThat(result).isNotNull(),
        () -> assertThat(result).hasSize(2),
        () -> assertThat(result.get(0)).isEqualTo("samples"),
        () -> assertThat(result.get(1)).isEqualTo("samples2")
    );
  }

  @ComponentScan(basePackages = {"samples", "samples2"})
  static class TestConfiguration {

  }

}