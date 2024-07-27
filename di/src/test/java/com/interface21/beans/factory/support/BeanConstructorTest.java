package com.interface21.beans.factory.support;

import com.interface21.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BeanConstructorTest {

    @Test
    void Autowired가_있는_생성자를_우선하여_사용한다() {
        BeanConstructor actual = BeanConstructor.createTargetConstructor(AutowiredClass.class);
        assertAll(
                () -> assertThat(actual.getParameterTypes()).hasSize(1),
                () -> assertThat(actual.getParameterTypes()).contains(int.class),
                () -> assertThat(actual.isNoArgument()).isFalse()
        );
    }

    @Test
    void Autowired가_없으면_기본생성자를_우선하여_사용한다() {
        BeanConstructor actual = BeanConstructor.createTargetConstructor(NoArgClass.class);
        assertAll(
                () -> assertThat(actual.getParameterTypes()).isEmpty(),
                () -> assertThat(actual.isNoArgument()).isTrue()
        );
    }

    @Test
    void Autowired와_기본생성자_모두_없으면_랜덤하여_선정한다() {
        assertDoesNotThrow(() -> BeanConstructor.createTargetConstructor(RandomClass.class));
    }

    public static class AutowiredClass {
        public AutowiredClass() {
        }

        @Autowired
        public AutowiredClass(int value) {
        }
    }

    public static class NoArgClass {
        public NoArgClass() {
        }

        public NoArgClass(int value) {
        }
    }

    public static class RandomClass {
        public RandomClass(String value) {
        }

        public RandomClass(int value) {
        }
    }
}
