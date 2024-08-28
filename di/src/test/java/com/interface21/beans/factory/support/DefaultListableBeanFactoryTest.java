package com.interface21.beans.factory.support;

import circular.MockCircularComponentA;
import circular.MockCircularComponentB;
import com.interface21.MockBeanFactory;
import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.JdbcTemplate;
import samples.SampleController;
import samples.SampleService;
import samples.config.ExampleConfig;
import samples.config.IntegrationConfig;

import javax.sql.DataSource;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultListableBeanFactoryTest {

    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        this.beanFactory = MockBeanFactory.createBeanFactory();
    }

    @Test
    @DisplayName("BeanFactory가 초기화되면 Bean이 생성된다")
    public void initTest() {

        // then
        Set<Class<?>> beanClasses = beanFactory.getBeanClasses();
        assertThat(beanClasses)
                .contains(SampleController.class, JdbcSampleRepository.class, SampleService.class);
    }

    @Test
    public void di() {

        // when
        final var sampleController = beanFactory.getBean(SampleController.class);

        assertNotNull(sampleController);
        assertNotNull(sampleController.getSampleService());

        final var sampleService = sampleController.getSampleService();
        assertNotNull(sampleService.getSampleRepository());
    }


    @Test
    @DisplayName("조회한 빈이 없으면 생성해서 반환한다")
    public void getBeanTest() {

        beanFactory = MockBeanFactory.createBeanFactory();

        SampleController bean = beanFactory.getBean(SampleController.class);

        assertNotNull(bean);
    }

    @Test
    @DisplayName("빈 생성시 의존성은 참조 방향의 종단부터 역순으로 생성되므로 주입할 빈의 생성이 먼저 완료되지 않으면 예외가 발생한다")
    public void recursiveInstantiationFailTest() {

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition(SampleController.class, new AnnotationBeanDefinition(SampleController.class));

        assertThrows(BeanInstantiationException.class,
                () -> factory.getBean(SampleController.class),
                " No BeanDefinition found for SampleService");
    }

    @Test
    @DisplayName("순환 참조가 있는 빈을 생성하면 예외가 발생한다")
    public void circularDependencyTest() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition(MockCircularComponentA.class, new AnnotationBeanDefinition(MockCircularComponentA.class));
        factory.registerBeanDefinition(MockCircularComponentB.class, new AnnotationBeanDefinition(MockCircularComponentB.class));

        assertThrows(BeanInstantiationException.class,
                factory::initialize,
                "Circular reference detected");
    }

    @Test
    @DisplayName("ConfigurationBeanDefinitionScanner는 @Bean 빈 정보를 등록한다")
    public void configurationBeanTest() {

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        int scan = new ConfigurationBeanDefinitionScanner(factory).scan(new String[]{"samples.config"});

        assertAll(() -> {
            assertThat(scan).isEqualTo(2);
            assertThat(factory.getBeanClasses())
                    .containsExactlyInAnyOrder(
                            JdbcTemplate.class,
                            DataSource.class);

        });
    }

}
