package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;

class DefaultBeanRegistryTest {

    private SampleController controller =
            new SampleController(
                    new SampleService(new JdbcSampleRepository()));

    @Test
    @DisplayName("빈을 등록한다")
    public void registerTest() {

        DefaultBeanRegistry defaultBeanRegistry = new DefaultBeanRegistry();
        defaultBeanRegistry.registeredBean(controller);

        assertTrue(defaultBeanRegistry.registeredBean(controller));
    }

    @Test
    @DisplayName("같은 타입의 객체를 여러번 등록하면 먼저 첫번째 등록된 빈이 등록된다")
    public void registerFailedWhenDuplicatedBeanRegisterTest() {

        DefaultBeanRegistry defaultBeanRegistry = new DefaultBeanRegistry();
        defaultBeanRegistry.registeredBean(controller);
        defaultBeanRegistry.registeredBean(
                new SampleController(
                        new SampleService(new JdbcSampleRepository())));

        assertThat(defaultBeanRegistry.getBean(SampleController.class)).isEqualTo(controller);
    }
}
