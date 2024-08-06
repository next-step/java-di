package com.interface21.context.support;

import com.interface21.context.ApplicationContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;
import samples.MyConfiguration;
import samples.SampleController;
import samples.SampleService;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationConfigWebApplicationContextTest {

    @Test
    @DisplayName("특정 패키지 밑의 bean 클래스를 클래스 타입으로 받환 받을 수 있다.")
    void getBeanTest() {
        final ApplicationContext applicationContext = new AnnotationConfigWebApplicationContext(MyConfiguration.class);

        final SampleController sampleController = applicationContext.getBean(SampleController.class);

        assertThat(sampleController).isNotNull();
    }

    @Test
    @DisplayName("특정 패키지 밑의 모든 bean 클래스들을 반환 받을 수 있다.")
    void getBeanClassesTest() {
        final ApplicationContext applicationContext = new AnnotationConfigWebApplicationContext(MyConfiguration.class);

        assertThat(applicationContext.getBeanClasses()).containsExactlyInAnyOrder(
                SampleController.class,
                SampleService.class,
                JdbcSampleRepository.class,
                MyConfiguration.class,
                JdbcTemplate.class,
                DataSource.class
        );
    }
}
