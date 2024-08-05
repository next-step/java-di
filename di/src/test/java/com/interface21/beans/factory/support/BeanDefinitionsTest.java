package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.beans.factory.config.BeanDefinition;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.SampleService;
import samples.TestDataSource;
import samples.TestDataSource2;

class BeanDefinitionsTest {
    @DisplayName("등록된 BeanDefinitions의 type을 추출한다.")
    @Test
    void extractTypes() {
        BeanDefinitions beanDefinitions = new BeanDefinitions();
        beanDefinitions.register(new ComponentBeanDefinition(SampleService.class));

        Set<Class<?>> beanTypes = beanDefinitions.extractTypes();

        assertThat(beanTypes).containsOnly(SampleService.class);
    }

    @DisplayName("타입이 완전히 일치하는 BeanDefinition 을 우선적으로 찾는다.")
    @Test
    void getByType() {
        BeanDefinitions beanDefinitions = new BeanDefinitions();
        beanDefinitions.register(new ComponentBeanDefinition(TestDataSource.class));
        beanDefinitions.register(new ComponentBeanDefinition(DataSource.class));

        BeanDefinition beanDefinition = beanDefinitions.getByType(DataSource.class);

        assertAll(
                () -> assertThat(beanDefinition.getType()).isEqualTo(DataSource.class),
                () -> assertThat(beanDefinition.getName()).isEqualTo("dataSource")
        );
    }

    @DisplayName("타입이 완전히 일치하는 BeanDefinition 이 없고, 인터페이스로 찾을 경우 인터페이스를 구현한 BeanDefinition을 찾는다.")
    @Test
    void getByType2() {
        BeanDefinitions beanDefinitions = new BeanDefinitions();
        beanDefinitions.register(new ComponentBeanDefinition(TestDataSource.class));

        BeanDefinition beanDefinition = beanDefinitions.getByType(DataSource.class);

        assertAll(
                () -> assertThat(beanDefinition.getType()).isEqualTo(TestDataSource.class),
                () -> assertThat(beanDefinition.getName()).isEqualTo("testDataSource")
        );
    }

    @DisplayName("타입이 완전히 일치하는 BeanDefinition 이 없고, 인터페이스로 찾을 경우 구현체가 2개 이상이면 예외를 발생시킨다.")
    @Test
    void getByType3() {
        BeanDefinitions beanDefinitions = new BeanDefinitions();
        beanDefinitions.register(new ComponentBeanDefinition(TestDataSource.class));
        beanDefinitions.register(new ComponentBeanDefinition(TestDataSource2.class));

        assertThatThrownBy(() -> beanDefinitions.getByType(DataSource.class))
                .isInstanceOf(IllegalArgumentException.class);
    }
}