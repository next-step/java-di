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
    @DisplayName("AnnotationConfigWebApplicationContext 스프링 컨테이너가 초기화되면 빈 인스턴스가 컨테이너에 등록된다")
    public void refreshTest() {

        assertThat(sut.getBeanClasses()).contains(SampleController.class);
        assertThat(sut.getBean(SampleController.class)).isNotNull();
    }
}
