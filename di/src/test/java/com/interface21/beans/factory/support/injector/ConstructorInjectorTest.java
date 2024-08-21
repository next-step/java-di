package com.interface21.beans.factory.support.injector;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.beans.factory.support.ClasspathBeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.SampleRepository;
import samples.SampleService;

class ConstructorInjectorTest {

    private DefaultListableBeanFactory beanFactory;
    private ClasspathBeanScanner classpathBeanScanner;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        beanFactory = new DefaultListableBeanFactory();
        classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        classpathBeanScanner.scan("samples");
    }


    @DisplayName("Constructor을 통해서 bean 주입을 합니다.")
    @Test
    void constructorInject() {
        ConstructorInjector injector = new ConstructorInjector(
            SampleService.class.getConstructors()[0]);
        SampleService service = (SampleService) injector.inject(beanFactory);

        assertThat(service).isInstanceOf(SampleService.class);
        assertThat(service.getSampleRepository()).isInstanceOf(SampleRepository.class);
    }
}
