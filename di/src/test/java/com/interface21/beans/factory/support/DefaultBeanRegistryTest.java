package com.interface21.beans.factory.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleDataSource;
import samples.SampleService;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBeanRegistryTest {

    private SampleController controller = new SampleController(new SampleService(new JdbcSampleRepository(new SampleDataSource())));

    @Test
    @DisplayName("빈을 등록한다")
    public void registerTest() {

        DefaultBeanRegistry defaultBeanRegistry = new DefaultBeanRegistry();
        defaultBeanRegistry.registerBean(controller);

        assertTrue(defaultBeanRegistry.registeredBean(controller));
    }

    @Test
    @DisplayName("이미 등록된 빈은 다시 등록할 수 없다")
    public void registerFailedWhenDuplicatedBeanRegisterTest() {

        DefaultBeanRegistry defaultBeanRegistry = new DefaultBeanRegistry();
        defaultBeanRegistry.registerBean(controller);

        assertThrows(IllegalStateException.class, () -> defaultBeanRegistry.registerBean(controller));
    }


}