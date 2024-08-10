package com.interface21.context.support;

import com.interface21.context.stereotype.Service;
import java.util.Map;
import javax.sql.DataSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.IntegrationConfig;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;
import samples.SampleComponent;
import samples.SampleController;
import samples.SampleFieldService;
import samples.SampleMethodService;
import samples.SampleService;

@DisplayName("AnnotationConfigWebApplicationContext 클래스는")
class AnnotationConfigWebApplicationContextTest {

    private AnnotationConfigWebApplicationContext context;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigWebApplicationContext(IntegrationConfig.class);
    }

    @DisplayName("Bean을 정상적으로 가져올 수 있어야 한다")
    @Test
    void testGetBean() {
        // when
        SampleService service = context.getBean(SampleService.class);

        // then
        assertNotNull(service);
    }

    @DisplayName("특정 어노테이션을 가진 Bean을 가져올 수 있어야 한다")
    @Test
    void testGetBeansWithAnnotation() {
        // when
        Map<Class<?>, Object> beans = context.getBeansWithAnnotation(Service.class);

        // then
        assertFalse(beans.isEmpty());
        assertTrue(beans.containsKey(SampleService.class));
    }

    @DisplayName("Bean 클래스의 목록을 가져올 수 있어야 한다")
    @Test
    void testGetBeanClasses() {
        // when
        var beanClasses = context.getBeanClasses();

        // then
        assertFalse(beanClasses.isEmpty());
        assertTrue(beanClasses.contains(DataSource.class));
        assertTrue(beanClasses.contains(JdbcTemplate.class));
        assertTrue(beanClasses.contains(SampleComponent.class));
        assertTrue(beanClasses.contains(SampleController.class));
        assertTrue(beanClasses.contains(SampleMethodService.class));
        assertTrue(beanClasses.contains(SampleFieldService.class));
        assertTrue(beanClasses.contains(SampleService.class));
        assertTrue(beanClasses.contains(JdbcSampleRepository.class));
    }
}
