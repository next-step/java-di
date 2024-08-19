package com.interface21.beans.factory.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.SampleController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultListableBeanFactoryTest {

    private DefaultListableBeanFactory beanFactory;
    private ClasspathBeanScanner classpathBeanScanner;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        beanFactory = new DefaultListableBeanFactory();
        classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        classpathBeanScanner.scan("samples");
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


}
