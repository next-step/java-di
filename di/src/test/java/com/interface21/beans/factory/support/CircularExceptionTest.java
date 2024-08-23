package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.beans.factory.CircularException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CircularExceptionTest {

    private DefaultListableBeanFactory beanFactory;
    private ClasspathBeanScanner classpathBeanScanner;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        beanFactory = new DefaultListableBeanFactory();
        classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        classpathBeanScanner.scan("circular");
    }

    @Test
    @DisplayName("Bean의 Circular Exception을 예외 처리합니다.")
    public void circularException() {
        assertThatThrownBy(() -> beanFactory.initialize())
            .isInstanceOf(CircularException.class)
            .hasMessageContaining(
                "[[class circular.SampleService1, class circular.SampleService2]]");

    }
}
