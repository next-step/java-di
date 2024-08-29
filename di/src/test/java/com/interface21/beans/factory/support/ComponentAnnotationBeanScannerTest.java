package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;

class ComponentAnnotationBeanScannerTest{

    @Test
    void Component_어노테이션을_포함한_클래스만_반환해야_한다() {
      ComponentAnnotationBeanScanner scanner = new ComponentAnnotationBeanScanner("samples");
      Set<Class<?>> configClasses = scanner.getComponentAnnotationClasses();

      assertNotNull(configClasses);
      assertTrue(configClasses.contains(JdbcSampleRepository.class));
    }

}