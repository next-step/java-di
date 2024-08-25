package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import javax.sql.DataSource;

import com.interface21.MockBeanFactory;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.*;

class ConstructorResolverTest {

    private BeanFactory factory;

    @BeforeEach
    void setUp() {
        factory = MockBeanFactory.createBeanFactory();
    }

    @Test
    @DisplayName("ConstructorResolver는 @Autowired 셍성자 정보를 제공한다")
    public void resolveTest() {

        ConstructorHolder constructorHolder = ConstructorResolver.resolve(SampleService.class);

        Constructor<?> constructor = constructorHolder.constructor();
        assertNotNull(constructor);
        assertThat(constructor.getParameterCount()).isEqualTo(1);
        assertThat(constructor.getParameters()[0].getType()).isEqualTo(SampleRepository.class);
    }

    @Test
    @DisplayName("ConstructorResolver는 @Autowired 셍성자가 없으면 선언된 생성자 리스트 중 첫번째 생성자의 정보를 제공한다")
    public void resolveTest2() {

        ConstructorHolder constructorHolder = ConstructorResolver.resolve(JdbcSampleRepository.class);

        Constructor<?> constructor = constructorHolder.constructor();

        assertNotNull(constructor);
        assertThat(constructor.getParameterCount()).isEqualTo(0);
//        assertThat(constructor.getParameters()[0].getType()).isEqualTo(DataSource.class);
    }
}
