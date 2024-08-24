package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.beans.factory.config.BeanDefinition;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;

class BeanScannerTest {

  @Test
  @DisplayName("@Repository, @Service, @Controller 어노테이션이 존재하는 모든 클래스를 스캔한다")
  void scanTest() {
    List<String> basePackages = List.of("samples");
    BeanScanner scanner = new BeanScanner(basePackages);

    Map<Class<?>, BeanDefinition> result = scanner.scan();

    assertAll(
        () -> assertThat(result).isNotNull(),
        () -> assertThat(result).isNotEmpty(),
        () -> assertThat(result.containsKey(JdbcSampleRepository.class)).isTrue(),
        () -> assertThat(result.containsKey(SampleController.class)).isTrue(),
        () -> assertThat(result.containsKey(SampleService.class)).isTrue()
    );
  }

}