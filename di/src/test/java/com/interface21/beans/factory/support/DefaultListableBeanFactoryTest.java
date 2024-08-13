package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.context.support.BeanScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.MyConfiguration;
import samples.SampleController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultListableBeanFactoryTest {

    private BeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        beanFactory = new DefaultListableBeanFactory();

        BeanScanner beanScanner = new BeanScanner(beanFactory);
        beanScanner.register(MyConfiguration.class);

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
