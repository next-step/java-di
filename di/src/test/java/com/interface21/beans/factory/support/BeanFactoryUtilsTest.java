package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.interface21.context.annotation.Bean;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeanFactoryUtilsTest {

  @Test
  @DisplayName("@Bean 어노테이션이 붙어있는 메서드 리스트를 반환한다")
  void getBeanMethodsTest() {
    Set<Method> beanMethods = BeanFactoryUtils.getBeanMethods(FooClass.class, Bean.class);

    assertAll(
        () -> assertThat(beanMethods).isNotNull(),
        () -> assertThat(beanMethods).isNotEmpty(),
        () -> assertThat(beanMethods).hasSize(2)
    );
  }

  static class FooClass {

    @Bean
    public void bar() {

    }

    @Bean
    public void baz() {

    }

    public void qux() {

    }
  }
}