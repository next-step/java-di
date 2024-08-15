package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.SampleRepository;
import samples.SampleService;

class BeanFactoryUtilsTest {

    @Test
    @DisplayName("인자로 전달하는 클래스의 생성자 중 @Inject 애노테이션이 설정되어 있는 생성자를 반환한다")
    public void getInjectedConstructorTest() {

        Constructor<?> injectedConstructor =
                BeanFactoryUtils.getInjectedConstructor(SampleService.class);

        Parameter[] parameters = injectedConstructor.getParameters();

        assertThat(parameters).hasSize(1);
        assertThat(parameters[0].getType()).isEqualTo(SampleRepository.class);
    }

    @Test
    @DisplayName("인자로 전달되는 Class가 인터페이스라면 해당 인터페이스 구현체의 클래스가 반환된다")
    public void findConcreteClassTest() {

        // given
        MockSampleRepository mockSampleRepository = new MockSampleRepository();
        Set<Class<?>> registeredBeans = Set.of(mockSampleRepository.getClass());

        // when
        Optional<Class<?>> concreteClass =
                BeanFactoryUtils.findConcreteClass(SampleRepository.class, registeredBeans);

        // then
        assertThat(concreteClass).isPresent();
        assertThat(concreteClass.get()).isEqualTo(mockSampleRepository.getClass());
    }

    class MockSampleRepository implements SampleRepository {}
}
