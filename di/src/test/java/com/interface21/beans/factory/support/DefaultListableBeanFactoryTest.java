package com.interface21.beans.factory.support;

import com.interface21.context.support.BeanScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.SampleController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultListableBeanFactoryTest {

    private DefaultListableBeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        BeanScanner beanScanner = new BeanScanner("samples");
        beanScanner.initialize();

        beanFactory = new DefaultListableBeanFactory(beanScanner);
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
