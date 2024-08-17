package com.interface21.beans.factory.support.injector;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.beans.factory.support.BeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleRepository;

class DefaultInjectorTest {
    private DefaultListableBeanFactory beanFactory;
    private BeanScanner beanScanner;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        beanFactory = new DefaultListableBeanFactory();
        beanScanner = new BeanScanner(beanFactory);
        beanScanner.scan("samples");
    }


    @DisplayName("Constructor을 통해서 bean 주입을 합니다.")
    @Test
    void constructorInject(){
        DefaultInjector injector = new DefaultInjector(JdbcSampleRepository.class);
        JdbcSampleRepository repository = (JdbcSampleRepository) injector.inject(beanFactory);

        assertThat(repository).isInstanceOf(JdbcSampleRepository.class);
        assertThat(repository).isInstanceOf(SampleRepository.class);
    }
}
