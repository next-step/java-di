package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.beans.factory.annotation.Autowired;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeanFactoryUtilsTest {
    private static class TestClass {
        public TestClass() {
        }

        @Autowired
        public TestClass(String ignore) {
        }
    }

    @DisplayName("@Autowired 가 설정돼 있는 생성자를 반환한다.")
    @Test
    void getAutowiredConstructor() {
        Constructor<?> autowiredConstructor = BeanFactoryUtils.getAutowiredConstructor(TestClass.class);
        assertThat(autowiredConstructor.getParameters()[0].getType()).isEqualTo(String.class);
    }

    private interface TestInterface {}
    private static class TestImpl implements TestInterface {}
    private static class TestNotImpl {}

    @DisplayName("clazz가 인터페이스인 경우 List에서 구체 클래스를 찾아서 반환")
    @Test
    void findConcreteClassInterface() {
        Class<TestInterface> testInterface = TestInterface.class;
        List<Class<?>> classes = List.of(TestImpl.class, TestNotImpl.class);

        Optional<Class<?>> concreteClass = BeanFactoryUtils.findConcreteClass(testInterface, classes);

        assertThat(concreteClass.get()).isEqualTo(TestImpl.class);
    }


    @DisplayName("clazz가 인터페이스가 아닌 경우 그대로 반환")
    @Test
    void findConcreteClassNotInterface() {
        Class<TestClass> testClass = TestClass.class;
        List<Class<?>> classes = List.of(TestImpl.class, TestNotImpl.class);

        Optional<Class<?>> concreteClass = BeanFactoryUtils.findConcreteClass(testClass, classes);

        assertThat(concreteClass.get()).isEqualTo(TestClass.class);
    }

    @DisplayName("clazz가 Null이면 빈 Optional 반환")
    @Test
    void findConcreteClassNull() {
        Optional<Class<?>> concreteClass = BeanFactoryUtils.findConcreteClass(null, List.of());

        assertThat(concreteClass).isEmpty();
    }
}