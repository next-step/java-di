package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("InjectMode 클래스의")
class InjectModeTest {

    @DisplayName("getInjector 메서드는")
    @Nested
    class GetInjector {

        @DisplayName("CONSTRUCTOR일때  ConstructorInjector injector를 반환한다")
        @Test
        void constructor() {
            // given
            InjectMode injectMode = InjectMode.CONSTRUCTOR;

            // when
            Injector injector = injectMode.getInjector();

            // then
            assertTrue(injector instanceof ConstructorInjector);
        }

        @DisplayName("METHOD는 MethodInjector injector를 반환한다")
        @Test
        void method() {
            // given
            InjectMode injectMode = InjectMode.METHOD;

            // when
            Injector injector = injectMode.getInjector();

            // then
            assertTrue(injector instanceof MethodInjector);
        }

        @DisplayName("FIELD는 FieldInjector injector를 반환한다")
        @Test
        void field() {
            // given
            InjectMode injectMode = InjectMode.FIELD;

            // when
            Injector injector = injectMode.getInjector();

            // then
            assertTrue(injector instanceof FieldInjector);
        }

        @DisplayName("NONE은 DefaultInjector injector를 반환한다")
        @Test
        void none() {
            // given
            InjectMode injectMode = InjectMode.NONE;

            // when
            Injector injector = injectMode.getInjector();

            // then
            assertTrue(injector instanceof DefaultInjector);
        }
    }
}
