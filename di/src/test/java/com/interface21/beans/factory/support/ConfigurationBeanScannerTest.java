package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.*;

import com.interface21.beans.factory.BeanFactory;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import samples.ExampleConfig;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;

class ConfigurationBeanScannerTest {

  @Test
  void Bean_어노테이션이_있는_메소드를_포함한_Configuration_클래스만_반환해야_한다() {
    ConfigurationBeanScanner scanner = new ConfigurationBeanScanner("samples");
    Set<Class<?>> configClasses = scanner.getConfigurationClassesWithBean();

    assertNotNull(configClasses);
    assertTrue(configClasses.contains(ExampleConfig.class));
  }

  @Test
  void 빈_팩토리가_샘플_패키지의_빈들을_올바르게_생성하고_주입해야_한다() {

    var componentScanner = new ComponentAnnotationBeanScanner("samples");
    Set<Class<?>> componentsClass = componentScanner.getComponentAnnotationClasses();
    var configurationScanner = new ConfigurationBeanScanner("samples");
    Set<Class<?>> configurationsClass = configurationScanner.getConfigurationClassesWithBean();

    componentsClass.addAll(configurationsClass);

    // JdbcSampleRepository 빈 검증
    assertNotNull(componentsClass);
    assertTrue(componentsClass.contains(JdbcSampleRepository.class));

    // JdbcTemplate 빈 검증
  }

}