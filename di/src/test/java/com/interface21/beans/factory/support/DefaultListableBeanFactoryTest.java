package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.SampleController;

class DefaultListableBeanFactoryTest {

    private DefaultListableBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        beanFactory = new DefaultListableBeanFactory("samples");
        beanFactory.initialize();
    }

    @Test
    public void di() {
        final var sampleController = beanFactory.getBean(SampleController.class);
        final var sampleService = sampleController.getSampleService();

        assertAll(
            () -> assertThat(sampleController).isNotNull(),
            () -> assertThat(sampleController.getSampleService()).isNotNull(),
            () -> assertThat(sampleService.getSampleRepository()).isNotNull()
        );
    }

    @Test
    @DisplayName("@Controller 가 설정된 빈 목록들을 조회한다")
    void getControllersTest() {
        Map<Class<?>, Object> controllers = beanFactory.getControllers();

        assertAll(
            () -> assertThat(controllers).isNotNull(),
            () -> assertThat(controllers).hasSize(1),
            () -> assertThat(controllers.containsKey(SampleController.class)).isTrue()
        );
    }
}
