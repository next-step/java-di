package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import javax.sql.DataSource;

import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.*;

class ConstructorResolverTest {

    private final DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

    @BeforeEach
    void setUp() {

        factory.registerBeanDefinition(
                SampleController.class, new AnnotationBeanDefinition(SampleController.class));
        factory.registerBeanDefinition(
                SampleService.class, new AnnotationBeanDefinition(SampleService.class));
        factory.registerBeanDefinition(
                JdbcSampleRepository.class,
                new AnnotationBeanDefinition(JdbcSampleRepository.class));
        factory.registerBeanDefinition(
                SampleDataSource.class, new AnnotationBeanDefinition(SampleDataSource.class));

        factory.initialize();
    }

    @Test
    @DisplayName("@Autowired가 선언된 생성자를 반환한다")
    public void resolveTest() {

        ConstructorResolver constructorResolver = new ConstructorResolver(factory);
        Constructor<?> constructor = constructorResolver.resolveConstructor(SampleService.class);

        assertNotNull(constructor);
        assertThat(constructor.getParameterCount()).isEqualTo(1);
        assertThat(constructor.getParameters()[0].getType()).isEqualTo(SampleRepository.class);
    }

    @Test
    @DisplayName("@Autowired가 선언된 생성자가 없으면 첫 번째 생성자를 반환한다")
    public void resolveTest2() {

        ConstructorResolver constructorResolver = new ConstructorResolver(factory);

        Constructor<?> constructor =
                constructorResolver.resolveConstructor(JdbcSampleRepository.class);

        assertNotNull(constructor);
        assertThat(constructor.getParameterCount()).isEqualTo(1);
        assertThat(constructor.getParameters()[0].getType()).isEqualTo(DataSource.class);
    }
}
