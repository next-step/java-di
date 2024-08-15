package com.interface21.beans.factory.support;

import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BeansTest {

    @DisplayName("특정 어노테이션을 가진 빈을 찾는다.")
    @Test
    void findBeansWithAnnotation() {
        // given
        final var beans = new Beans();
        beans.addBean("sampleController", new SampleController());
        beans.addBean("sampleService", new SampleService());

        // when
        final var result = beans.findBeansWithAnnotation(Controller.class);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).containsKeys(SampleController.class)
        );
    }

    @Controller
    static class SampleController {

    }

    @Service
    static class SampleService {

    }
}
