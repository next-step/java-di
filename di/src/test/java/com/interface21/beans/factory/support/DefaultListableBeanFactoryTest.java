package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.SingletonBeanDefinition;
import com.interface21.context.annotation.Scope;
import com.interface21.context.stereotype.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.SampleController;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultListableBeanFactoryTest {

    private Reflections reflections;
    private DefaultListableBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        reflections = new Reflections("samples");
        beanFactory = new DefaultListableBeanFactory();
        beanFactory.initialize();
    }

    @Test
    void beanDefinition을_받아_저장한다() {
        beanFactory.registerBeanDefinition(TestController.class, new SingletonBeanDefinition(TestController.class));
        assertThat(beanFactory.getBeanDefinitionMap()).containsKey("TestController");
    }

    @Test
    void component이_아닌_클래스가_저장요청된_경우_예외가_발생한다() {
        assertThatThrownBy(() -> beanFactory.registerBeanDefinition(NoComponentController.class, new SingletonBeanDefinition(NoComponentController.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Component가 있는 클래스만 저장할 수 있습니다.");
    }

    @Test
    public void di() {
        final var sampleController = beanFactory.getBean(SampleController.class);

        assertNotNull(sampleController);
        assertNotNull(sampleController.getSampleService());

        final var sampleService = sampleController.getSampleService();
        assertNotNull(sampleService.getSampleRepository());
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = new HashSet<>();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    @Controller
    public class TestController {

    }

    @Scope
    public class NoComponentController {

    }
}
