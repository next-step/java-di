package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.beans.factory.config.GenericBeanDefinition;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.MyConfiguration;
import samples.SampleController;

class SimpleBeanDefinitionRegistryTest {

  private SimpleBeanDefinitionRegistry beanDefinitionRegistry;

  @BeforeEach
  void setUp() throws Exception {
    beanDefinitionRegistry = new SimpleBeanDefinitionRegistry();
  }

  @Test
  @DisplayName("SampleController.class 를 BeanDefinition 객체로 BeanRegistry 에 등록한다")
  void register() {
    final GenericBeanDefinition beanDefinition = GenericBeanDefinition.from(SampleController.class);
    beanDefinitionRegistry.register(SampleController.class, beanDefinition);

    Map<Class<?>, BeanDefinition> result = beanDefinitionRegistry.getBeanDefinitions();

    assertAll(
        () -> assertThat(result).containsKey(SampleController.class),
        () -> assertThat(result).hasSize(1)
    );
  }

  @Test
  @DisplayName("DataSource.class 를 BeanDefinition 객체로 BeanRegistry 에 등록한다")
  void registerDataSource() throws NoSuchMethodException {
    final Method method = MyConfiguration.class.getMethod("dataSource");
    final ConfigurationBeanDefinition beanDefinition = new ConfigurationBeanDefinition(DataSource.class, method);
    beanDefinitionRegistry.register(DataSource.class, beanDefinition);

    final BeanDefinition result = beanDefinitionRegistry.get(DataSource.class);

    assertAll(
        () -> assertThat(result).isInstanceOf(ConfigurationBeanDefinition.class),
        () -> assertThat(result.getType()).isEqualTo(DataSource.class)
    );
  }

  @Test
  @DisplayName("등록한 BeanDefinition 객체를 조회한다")
  void success_get() {
    final GenericBeanDefinition beanDefinition = GenericBeanDefinition.from(SampleController.class);
    beanDefinitionRegistry.register(SampleController.class, beanDefinition);

    BeanDefinition result = beanDefinitionRegistry.get(SampleController.class);

    assertAll(
        () -> assertThat(result.getBeanClassName()).isEqualTo("sampleController"),
        () -> assertThat(result.getType()).isEqualTo(SampleController.class)
    );
  }

  @Test
  @DisplayName("등록한 BeanDefinition 객체가 없다면 에러를 던진다.")
  void fail_get() {
    assertThrows(BeanInstantiationException.class,
        () -> beanDefinitionRegistry.get(SampleController.class));
  }

  @Test
  @DisplayName("BeanDefinition 객체 map 들을 하나로 합쳐 내부 map 에 저장한다")
  void registerAll() throws NoSuchMethodException {
    Map<Class<?>, BeanDefinition> map1 = new HashMap<>();
    Method method = MyConfiguration.class.getMethod("dataSource");
    map1.put(DataSource.class, new ConfigurationBeanDefinition(DataSource.class, method));

    Map<Class<?>, BeanDefinition> map2 = new HashMap<>();
    map2.put(SampleController.class, GenericBeanDefinition.from(SampleController.class));

    beanDefinitionRegistry.registerAll(map1, map2);
    Map<Class<?>, BeanDefinition> result = beanDefinitionRegistry.getBeanDefinitions();

    assertAll(
        () -> assertThat(result).hasSize(2),
        () -> assertThat(result).containsKey(SampleController.class),
        () -> assertThat(result).containsKey(DataSource.class)
    );
  }

  @Test
  @DisplayName("BeanDefinition 객체들 중 동일한 bean 네임이 존재할 경우 에러를 던진다")
  void registerAllThrowBeanCreationException() throws NoSuchMethodException {
    Map<Class<?>, BeanDefinition> map1 = new HashMap<>();
    Method method = MyConfiguration.class.getMethod("dataSource");
    map1.put(DataSource.class, new ConfigurationBeanDefinition(SampleController.class, method));

    Map<Class<?>, BeanDefinition> map2 = new HashMap<>();
    map2.put(SampleController.class, GenericBeanDefinition.from(SampleController.class));

    assertThrows(BeanCreationException.class,
        () -> beanDefinitionRegistry.registerAll(map1, map2));
  }
}