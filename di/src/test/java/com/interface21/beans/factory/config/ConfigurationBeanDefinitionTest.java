package com.interface21.beans.factory.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.beans.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.MyConfiguration;

class ConfigurationBeanDefinitionTest {

  @Test
  @DisplayName("메서드로 빈을 생성한다")
  public void createBeanTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Function<Class<?>, Object> beanSupplier = type -> {
      if (type.equals(MyConfiguration.class)) {
        GenericBeanDefinition bean = GenericBeanDefinition.from(MyConfiguration.class);
        return BeanUtils.instantiateClass(bean.getConstructor(), bean.getConstructor().getTypeParameters());
      }
      throw new IllegalArgumentException("Unexpected type: " + type);
    };

    Method method = MyConfiguration.class.getMethod("dataSource");
    ConfigurationBeanDefinition beanDefinition = new ConfigurationBeanDefinition(DataSource.class, method);

    final Object result = beanDefinition.createBean(beanSupplier);

    assertThat(result).isNotNull();
  }

}