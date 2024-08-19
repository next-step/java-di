package com.interface21.context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.SampleController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AnnotationConfigWebApplicationContextTest {

    private AnnotationConfigWebApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.initialize();
    }

    @Test
    public void di() {
        final var sampleController = applicationContext.getBean(SampleController.class);

        assertNotNull(sampleController);
        assertNotNull(sampleController.getSampleService());

        final var sampleService = sampleController.getSampleService();
        assertNotNull(sampleService.getSampleRepository());
    }
}
