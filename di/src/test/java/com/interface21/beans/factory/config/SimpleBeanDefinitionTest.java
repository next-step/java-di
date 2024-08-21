package com.interface21.beans.factory.config;

import com.interface21.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleBeanDefinitionTest {

    @Test
    @DisplayName("생성자가 존재하지 않으면 기본 생성자를 반환한다.")
    void findBeanConstructorWithNoArgConstructor() {
        final SimpleBeanDefinition beanDefinition = SimpleBeanDefinition.from(NoArgConstructorClass.class);

        final Constructor<?> constructor = beanDefinition.getConstructor();

        assertThat(constructor.getParameterCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("생성자가 하나만 존재하면 해당 생성자를 반환한다.")
    void findBeanConstructorWithOneConstructor() {
        final SimpleBeanDefinition beanDefinition = SimpleBeanDefinition.from(OneConstructorClass.class);

        final Constructor<?> constructor = beanDefinition.getConstructor();

        assertThat(constructor.getParameterCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Autowired 가 붙은 생성자를 반환한다.")
    void findBeanConstructorWithAutowired() {
        final SimpleBeanDefinition beanDefinition = SimpleBeanDefinition.from(AutowiredConstructorClass.class);
        final Constructor<?> constructor = beanDefinition.getConstructor();

        assertThat(constructor.getParameterCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("createBean 은 생성자를 호출하여 빈을 생성한다.")
    void createBeanTest() throws Exception {
        final SimpleBeanDefinition beanDefinition = SimpleBeanDefinition.from(OneConstructorClass.class);

        final Object bean = beanDefinition.createBean(clazz -> "param");

        assertThat(bean).isInstanceOf(OneConstructorClass.class);
        assertThat(((OneConstructorClass) bean).param).isEqualTo("param");
    }

    public static class NoArgConstructorClass {
    }

    public static class OneConstructorClass {
        private final String param;
        public OneConstructorClass(final String param) {
            this.param = param;
        }
    }

    public static class AutowiredConstructorClass {
        public AutowiredConstructorClass() {
        }

        @Autowired
        public AutowiredConstructorClass(final String param) {
        }
    }
}