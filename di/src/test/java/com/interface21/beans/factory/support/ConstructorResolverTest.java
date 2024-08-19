package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.JdbcSampleRepository;
import samples.SampleRepository;
import samples.SampleService;

class ConstructorResolverTest {

    @Test
    @DisplayName("@Autowired가 선언된 생성자를 반환한다")
    public void resolveTest() {

        Constructor<?> constructor = ConstructorResolver.resolveConstructor(SampleService.class);

        assertNotNull(constructor);
        assertThat(constructor.getParameterCount()).isEqualTo(1);
        assertThat(constructor.getParameters()[0].getType()).isEqualTo(SampleRepository.class);
    }

    @Test
    @DisplayName("@Autowired가 선언된 생성자가 없으면 첫 번째 생성자를 반환한다")
    public void resolveTest2() {

        Constructor<?> constructor =
                ConstructorResolver.resolveConstructor(JdbcSampleRepository.class);

        assertNotNull(constructor);
        assertThat(constructor.getParameterCount()).isEqualTo(1);
        assertThat(constructor.getParameters()[0].getType()).isEqualTo(DataSource.class);
    }
}
