package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.NonComponentController;
import samples.SampleComponent;
import samples.SampleService;

class ComponentAnnotationBeanScannerTest{

  @Test
  void Repository_어노테이션을_포함한_클래스도_반환해야_한다() {

    ComponentAnnotationBeanScanner scanner = new ComponentAnnotationBeanScanner("samples");
    Set<Class<?>> components = scanner.scanForBeans();

    assertTrue(components.contains(JdbcSampleRepository.class));
  }

  @Test
  void Component_어노테이션이_없는_클래스는_반환하지_않아야_한다() {

    ComponentAnnotationBeanScanner scanner = new ComponentAnnotationBeanScanner("samples");
    Set<Class<?>> components = scanner.scanForBeans();

    assertFalse(components.contains(NonComponentController.class));
  }

  @Test
  void 빈_패키지_처리시_빈값을_리턴한다() {

    ComponentAnnotationBeanScanner emptyScanner = new ComponentAnnotationBeanScanner("empty.samples");
    Set<Class<?>> componentClasses = emptyScanner.scanForBeans();

    assertTrue(componentClasses.isEmpty());
  }

  @Test
  void Component_어노테이션을_포함한_클래스만_반환해야_한다() {
    ComponentAnnotationBeanScanner scanner = new ComponentAnnotationBeanScanner("samples");
    Set<Class<?>> components = scanner.scanForBeans();

    assertNotNull(components);
    assertTrue(components.contains(JdbcSampleRepository.class));
    assertTrue(components.contains(SampleComponent.class));
    assertTrue(components.contains(SampleService.class));
    assertFalse(components.contains(NonComponentController.class));
  }

}