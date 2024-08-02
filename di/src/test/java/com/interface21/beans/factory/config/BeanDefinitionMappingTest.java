package com.interface21.beans.factory.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

class BeanDefinitionMappingTest {


    @DisplayName("스캔한 Class 들을 BeanDefinition 으로 변환 한다")
    @Test
    public void toBeanDefinitionMapping() throws Exception {
        // given
        final BeanDefinitionMapping beanDefinitionMapping = new BeanDefinitionMapping();
        final Set<Class<?>> beanClasses = mock();

        // when then
        assertDoesNotThrow(() -> beanDefinitionMapping.toBeanDefinitionMap(beanClasses));
    }

    @DisplayName("모든 Bean 의 Class 를 반환 한다")
    @Test
    public void getBeanClasses() throws Exception {
        // given
        final BeanDefinitionMapping beanDefinitionMapping = new BeanDefinitionMapping();
        final Set<Class<?>> classes = Set.of(MockBean.class);
        beanDefinitionMapping.toBeanDefinitionMap(classes);

        // when
        final Set<Class<?>> actual = beanDefinitionMapping.getBeanClasses();

        // then
        assertThat(actual).hasSize(1)
                .contains(MockBean.class);
    }

    @DisplayName("모든 BeanDefinition 을 지운다")
    @Test
    public void clear() throws Exception {
        // given
        final BeanDefinitionMapping beanDefinitionMapping = new BeanDefinitionMapping();
        final Set<Class<?>> classes = Set.of(MockBean.class);
        beanDefinitionMapping.toBeanDefinitionMap(classes);

        // when
        beanDefinitionMapping.clear();

        // then
        final Set<Class<?>> actual = beanDefinitionMapping.getBeanClasses();
        assertThat(actual).isEmpty();
    }

    private static class MockBean {

    }
}
