package com.interface21.beans.factory.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.interface21.beans.factory.support.BeanInstantiationException;
import com.interface21.beans.factory.support.NoUniqueBeanDefinitionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.SampleService;

class GenericBeanDefinitionTest {

  @Test
  @DisplayName("@Autowired 어노테이션이 붙은 생성자를 가진 객체를 BeanDefinition 객체로 만든다")
  void withAutowiredConstructorTest() {
    final GenericBeanDefinition beanDefinition = GenericBeanDefinition.from(SampleService.class);
    final Constructor<?> constructor = beanDefinition.getConstructor();

    assertAll(
        () -> assertThat(beanDefinition).isNotNull(),
        () -> assertThat(constructor).isNotNull()
    );
  }

  @Test
  @DisplayName("기본 생성자를 가진 객체를 BeanDefinition 객체로 만든다")
  void testFrom_withDefaultConstructor() {
    final GenericBeanDefinition beanDefinition = GenericBeanDefinition.from(NoAutowiredService.class);
    final Constructor<?> constructor = beanDefinition.getConstructor();

    assertAll(
        () -> assertThat(beanDefinition).isNotNull(),
        () -> assertThat(constructor).isNotNull()
    );
  }

  @Test
  @DisplayName("@Autowired 어노테이션이 붙은 생성자가 여러개라면 에러가 발생한다")
  void noUniqueBeanDefinitionExceptionTest() {
    assertThrows(NoUniqueBeanDefinitionException.class,
        () -> GenericBeanDefinition.from(MultipleAutowiredService.class)
    );
  }

  @Test
  @DisplayName("생성자가 여러개라면 에러가 발생한다")
  void testFrom_beanInstantiationException() {
    assertThrows(BeanInstantiationException.class,
        () -> GenericBeanDefinition.from(NoConstructorService.class)
    );
  }

  @Test
  @DisplayName("생성자와 인자로 인스턴스를 생성한다")
  void instantiateClassTest()
      throws InvocationTargetException, IllegalAccessException, InstantiationException {
    final GenericBeanDefinition beanDefinition = GenericBeanDefinition.from(MyBean.class);

    Function<Class<?>, Object> beanSupplier = type -> {
      if (type.equals(String.class)) {
        return "TestName";
      } else if (type.equals(int.class)) {
        return 42;
      } else {
        throw new IllegalArgumentException("Unsupported type: " + type);
      }
    };

    final Object result = beanDefinition.createBean(beanSupplier);

    assertAll(
        () -> assertThat(result).isNotNull(),
        () -> assertThat(result).isInstanceOf(MyBean.class)
    );
  }

  static class MyBean {
    private final String name;
    private final int value;

    public MyBean(String name, int value) {
      this.name = name;
      this.value = value;
    }

    public String getName() {
      return name;
    }

    public int getValue() {
      return value;
    }
  }
}