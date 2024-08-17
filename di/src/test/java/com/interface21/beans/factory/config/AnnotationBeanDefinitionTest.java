package com.interface21.beans.factory.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.JdbcSampleRepository;

class AnnotationBeanDefinitionTest {

    @Test
    @DisplayName("AnnotationBeanDefinition#getType()은 빈의 타입을 반환한다")
    public void createTest() {

        AnnotationBeanDefinition bd = new AnnotationBeanDefinition(JdbcSampleRepository.class);

        assertThat(bd.getType()).isEqualTo(JdbcSampleRepository.class);
    }
}
