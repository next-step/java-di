package com.interface21.beans.factory.support;


import com.interface21.beans.BeanDefinitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BeanDefinitionsTest {

    private BeanDefinitions beanDefinitions;

    @BeforeEach
    void setUp() {
        final Set<Class<?>> beanClasses = Set.of(NoArgConstructorClass.class, OneConstructorClass.class);
        beanDefinitions = new BeanDefinitions(beanClasses);
    }

    @Test
    @DisplayName("모든 beanClass 들을 반환받을 수 있다.")
    void getBeanClassesTest() {
        assertThat(beanDefinitions.getBeanClasses()).containsExactlyInAnyOrder(
                NoArgConstructorClass.class, OneConstructorClass.class
        );
    }

    @Test
    @DisplayName("bean 의 생성자를 반환받을 수 있다.")
    void getBeanConstructorTest() {
        final Constructor<?> beanConstructor = beanDefinitions.getBeanConstructor(OneConstructorClass.class);

        assertThat(beanConstructor.getParameterCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 bean 의 생성자 요청 시 예외를 던진다.")
    void getBeanConstructorFailTest() {
        assertThatThrownBy(() -> beanDefinitions.getBeanConstructor(NotExistClass.class))
                .isInstanceOf(BeanDefinitionException.class)
                .hasMessageContaining("cannot find bean for");
    }

    @Test
    @DisplayName("beanDefinition 를 비울 수 있다.")
    void clearTest() {
        beanDefinitions.clear();

        assertThat(beanDefinitions.getBeanClasses()).isEmpty();
    }

    public static class NoArgConstructorClass {
    }

    public static class OneConstructorClass {
        public OneConstructorClass(final String param) {
        }
    }
    public static class NotExistClass {
    }
}
