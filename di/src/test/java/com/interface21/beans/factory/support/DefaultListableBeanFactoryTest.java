package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import samples.SampleController;

class DefaultListableBeanFactoryTest {

    private Reflections reflections;
    private DefaultListableBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        reflections = new Reflections("samples");
        beanFactory = new DefaultListableBeanFactory("samples");
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

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = new HashSet<>();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
