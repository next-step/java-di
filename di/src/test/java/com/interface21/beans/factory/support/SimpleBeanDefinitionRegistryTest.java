package com.interface21.beans.factory.support;


import com.interface21.beans.BeanDefinitionException;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.SimpleBeanDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimpleBeanDefinitionRegistryTest {

    private SimpleBeanDefinitionRegistry simpleBeanDefinitionRegistry;

    @BeforeEach
    void setUp() {
        final Set<Class<?>> beanClasses = Set.of(NoArgConstructorClass.class, OneConstructorClass.class);
        simpleBeanDefinitionRegistry = new SimpleBeanDefinitionRegistry(beanClasses);
    }

    @Test
    @DisplayName("모든 beanClass 들을 반환받을 수 있다.")
    void getBeanClassesTest() {
        assertThat(simpleBeanDefinitionRegistry.getBeanClasses()).containsExactlyInAnyOrder(
                NoArgConstructorClass.class, OneConstructorClass.class
        );
    }

    @Test
    @DisplayName("beanDefinition 을 반환받을 수 있다.")
    void getBeanDefinitionTest() {
        final BeanDefinition beanDefinition = simpleBeanDefinitionRegistry.getBeanDefinition(OneConstructorClass.class);

        assertThat(beanDefinition.getType()).isEqualTo(OneConstructorClass.class);
    }

    @Test
    @DisplayName("존재하지 않는 beanDefinition 요청 시 예외를 던진다.")
    void getBeanDefinitionFailTest() {
        assertThatThrownBy(() -> simpleBeanDefinitionRegistry.getBeanDefinition(NotExistClass.class))
                .isInstanceOf(BeanDefinitionException.class)
                .hasMessageContaining("cannot find bean for");
    }

    @Test
    @DisplayName("beanDefinition 를 비울 수 있다.")
    void clearTest() {
        simpleBeanDefinitionRegistry.clear();

        assertThat(simpleBeanDefinitionRegistry.getBeanClasses()).isEmpty();
    }

    @Test
    @DisplayName("beanDefinition 를 등록할 수 있다.")
    void registerBeanTest() {
        final SimpleBeanDefinitionRegistry simpleBeanDefinitionRegistry = new SimpleBeanDefinitionRegistry();

        simpleBeanDefinitionRegistry.registerBeanDefinition(NoArgConstructorClass.class, SimpleBeanDefinition.from(NoArgConstructorClass.class));

        assertThat(simpleBeanDefinitionRegistry.getBeanClasses()).containsExactly(NoArgConstructorClass.class);
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
