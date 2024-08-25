package com.interface21.beans.factory.support;

import com.interface21.MockBeanFactory;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.SampleController;
import samples.SampleService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AutowireAnnotationBeanInitializerTest {

    private BeanFactory factory;

    @BeforeEach
    void setUp() {
        factory = MockBeanFactory.createBeanFactory();
        ((DefaultListableBeanFactory) factory).initialize();
    }

    @Test
    @DisplayName("@Autowire 생성자가 있는 빈 초기화를 지원한다")
    void supportTest() {

        AutowireAnnotationBeanInitializer autowireAnnotationBeanInitializer = new AutowireAnnotationBeanInitializer(factory);

        assertTrue(autowireAnnotationBeanInitializer.support(new AnnotationBeanDefinition(SampleController.class)));
    }

    @Test
    @DisplayName("빈을 초기화시 의존성 주입을 수행한다")
    void initializeBeanTest() {

        AutowireAnnotationBeanInitializer autowireAnnotationBeanInitializer = new AutowireAnnotationBeanInitializer(factory);

        Object actual = autowireAnnotationBeanInitializer.initializeBean(new AnnotationBeanDefinition(SampleController.class));

        assertNotNull(actual);
        assertThat(actual).isInstanceOf(SampleController.class);
        assertNotNull(((SampleController) actual).getSampleService());
    }

    @Test
    @DisplayName("빈 초기화시 필요한 인자를 제공한다")
    void resolveArgumentsTest() {

        AutowireAnnotationBeanInitializer autowireAnnotationBeanInitializer = new AutowireAnnotationBeanInitializer(factory);

        Object[] arguments = autowireAnnotationBeanInitializer.resolveArguments(new AnnotationBeanDefinition(SampleController.class));

        assertThat(arguments).hasSize(1);
        assertThat(arguments[0]).isInstanceOf(SampleService.class);
    }

}