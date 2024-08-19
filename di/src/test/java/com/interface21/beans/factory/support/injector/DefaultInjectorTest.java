package com.interface21.beans.factory.support.injector;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.beans.factory.support.ClasspathBeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.SampleComponent;

class DefaultInjectorTest {

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
        DefaultInjector injector = new DefaultInjector(SampleComponent.class);
        SampleComponent component = (SampleComponent) injector.inject(beanFactory);

        assertThat(component).isInstanceOf(SampleComponent.class);
    }
}
