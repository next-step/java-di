package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.ExampleConfig;
import samples.ExampleFailConfig;
import samples.JdbcSampleRepository;

class ConfigurationBeanScannerTest {
  private ConfigurationBeanScanner scanner;

  @BeforeEach
  void initilaize() {
    scanner = new ConfigurationBeanScanner("samples");
  }

  @Test
  void Bean_어노테이션이_있는_메소드를_포함한_Configuration_클래스만_반환해야_한다() {
    Set<Class<?>> configClasses = scanner.scanForBeans();

    assertNotNull(configClasses);
    assertTrue(configClasses.contains(ExampleConfig.class));
    assertFalse(configClasses.contains(ExampleFailConfig.class));
  }

  @Test
  void Bean_어노테이션이_없는_메소드만_있는_Configuration_클래스는_반환하지_않아야_한다() {
    Set<Class<?>> configClasses = scanner.scanForBeans();

    assertFalse(configClasses.contains(ExampleFailConfig.class));
  }

  @Test
  void Configuration_어노테이션이_없는_클래스는_반환하지_않아야_한다() {
    Set<Class<?>> configClasses = scanner.scanForBeans();

    assertFalse(configClasses.contains(ExampleFailConfig.class));
  }

  @Test
  void 빈_패키지를_처리할_수_있어야_한다() {
    ConfigurationBeanScanner emptyScanner = new ConfigurationBeanScanner("empty");
    Set<Class<?>> configClasses = emptyScanner.scanForBeans();

    assertTrue(configClasses.isEmpty());
  }
  @Test
  void 빈_팩토리가_샘플_패키지의_빈들을_올바르게_생성하고_주입해야_한다() {

    List<BeanScanner> scanners = Arrays.asList(
        new ComponentAnnotationBeanScanner("samples"),
        new ConfigurationBeanScanner("samples")
    );

    Set<Class<?>> beanClasses = scanners.stream()
        .flatMap(scanner -> scanner.scanForBeans().stream())
        .collect(Collectors.toSet());

    // JdbcSampleRepository 빈 검증
    assertNotNull(beanClasses);
    assertTrue(beanClasses.contains(JdbcSampleRepository.class));

  }
}
