package com.interface21.beans.factory.support;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.interface21.context.support.ComponentScanner;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.SampleController;
import samples.TestConfig;

class DefaultListableBeanFactoryTest {

    private Reflections reflections;
    private DefaultListableBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        reflections = new Reflections("samples");
        beanFactory = new DefaultListableBeanFactory(ComponentScanner.from(TestConfig.class).scan());
        beanFactory.initialize();
    }

    @Test
    void di() {
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
}
