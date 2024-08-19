package com.interface21.context.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.SampleController;

class AnnotationConfigWebApplicationContextTest {

    private AnnotationConfigWebApplicationContext sut;

    @BeforeEach
    void setUp() {
        sut = new AnnotationConfigWebApplicationContext("samples");
    }

    @Test
    @DisplayName("AnnotationConfigWebApplicationContext#refresh()를 호출하면 스프링 컨테이너가 초기화된다")
    public void refreshTest() {

        sut.refresh();

        assertThat(sut.getBeanClasses()).contains(SampleController.class);
        assertThat(sut.getBean(SampleController.class)).isNotNull();
    }
}
