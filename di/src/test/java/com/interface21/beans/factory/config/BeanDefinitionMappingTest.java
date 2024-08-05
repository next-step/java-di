package com.interface21.beans.factory.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BeanDefinitionMappingTest {

    @DisplayName("빈을 스캔 후 BeanDefinition 으로 변환 하여 상태 값으로 가진다")
    @Test
    public void scanBeanDefinitions() throws Exception {
        // given
        final BeanDefinitionMapping beanDefinitionMapping = new BeanDefinitionMapping("samples");

        // when then
        assertDoesNotThrow(beanDefinitionMapping::scanBeanDefinitions);
    }

    @DisplayName("모든 Bean 의 Class 를 반환 한다")
    @Test
    public void getBeanClasses() throws Exception {
        // given
        final BeanDefinitionMapping beanDefinitionMapping = new BeanDefinitionMapping("samples");
        beanDefinitionMapping.scanBeanDefinitions();

        // when
        final Set<Class<?>> actual = beanDefinitionMapping.getBeanClasses();

        // then
        assertThat(actual).hasSize(8);
    }

    @DisplayName("모든 BeanDefinition 을 지운다")
    @Test
    public void clear() throws Exception {
        // given
        final BeanDefinitionMapping beanDefinitionMapping = new BeanDefinitionMapping("samples");
        beanDefinitionMapping.scanBeanDefinitions();

        // when
        beanDefinitionMapping.clear();

        // then
        final Set<Class<?>> actual = beanDefinitionMapping.getBeanClasses();
        assertThat(actual).isEmpty();
    }
}
