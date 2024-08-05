package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.interface21.context.stereotype.Controller;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.CircularA;
import samples.CircularB;
import samples.CircularC;
import samples.CrossReferenceA;
import samples.CrossReferenceB;
import samples.InterfaceAImpl;
import samples.InterfaceBImpl;
import samples.SampleController;
import samples.SampleScanConfig;

class DefaultListableBeanFactoryTest {

    private Reflections reflections;
    private DefaultListableBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        reflections = new Reflections("samples");
        beanFactory = new DefaultListableBeanFactory(SampleScanConfig.class);
        beanFactory.initialize();
    }

    @Test
    public void di() {
        final var sampleController = beanFactory.getBean(SampleController.class);

        assertNotNull(sampleController);
        assertNotNull(sampleController.getSampleService());

        final var sampleService = sampleController.getSampleService();
        assertNotNull(sampleService.getSampleRepository());
    }

    @DisplayName("특정 Annotation이 달려있는 Bean을 찾는다.")
    @Test
    void getBeansAnnotatedWith() {
        Map<Class<?>, Object> controllerBeans = beanFactory.getBeansAnnotatedWith(Controller.class);
        assertThat(controllerBeans).containsOnlyKeys(SampleController.class);
    }

    @DisplayName("Bean으로 등록되지 않은 클래스는 getBean을 호출하면 예외를 발생시킨다")
    @Test
    void getBeanException() {
        assertThatThrownBy(() -> beanFactory.getBean(Object.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("순환 참조가 발생한 경우 예외를 발생시킨다. [case: A <-> B 상호 참조]")
    @Test
    void circularReferenceTest1() {
        assertAll(
                () -> assertThatThrownBy(() -> beanFactory.getBean(CrossReferenceA.class)).isInstanceOf(CircularReferenceException.class),
                () -> assertThatThrownBy(() -> beanFactory.getBean(CrossReferenceB.class)).isInstanceOf(CircularReferenceException.class)
        );
    }

    @DisplayName("순환 참조가 발생한 경우 예외를 발생시킨다. [case: A -> B -> C -> A 참조]")
    @Test
    void circularReferenceTest2() {
        assertAll(
                () -> assertThatThrownBy(() -> beanFactory.getBean(CircularA.class)).isInstanceOf(CircularReferenceException.class),
                () -> assertThatThrownBy(() -> beanFactory.getBean(CircularB.class)).isInstanceOf(CircularReferenceException.class),
                () -> assertThatThrownBy(() -> beanFactory.getBean(CircularC.class)).isInstanceOf(CircularReferenceException.class)
        );
    }

    @DisplayName("인터페이스에 대해서도 순환 참조를 감지할 수 있다. [case: A <-> B 참조, 파라미터 인터페이스]")
    @Test
    void circularReferenceTest3() {
        assertAll(
                () -> assertThatThrownBy(() -> beanFactory.getBean(InterfaceAImpl.class)).isInstanceOf(CircularReferenceException.class),
                () -> assertThatThrownBy(() -> beanFactory.getBean(InterfaceBImpl.class)).isInstanceOf(CircularReferenceException.class)
        );
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = new HashSet<>();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
