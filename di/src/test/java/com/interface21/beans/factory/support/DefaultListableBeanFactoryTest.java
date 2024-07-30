package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultListableBeanFactoryTest {

    private DefaultListableBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        final BeanScanner beanScanner = new BeanScanner("samples");
        this.beanFactory = new DefaultListableBeanFactory(beanScanner.scan());
        beanFactory.initialize();
    }

    @Test
    public void di() {
        final var sampleController = beanFactory.getBean(SampleController.class);

        assertNotNull(sampleController);
        assertNotNull(sampleController.getSampleService());

        final var sampleService = sampleController.getSampleService();
        assertNotNull(sampleService.getSampleRepository());
    }

    @Test
    @DisplayName("bean 들의 모든 class 정보를 가져올 수 있다.")
    void getBeanClassesTest() {
        final Set<Class<?>> beanClasses = beanFactory.getBeanClasses();

        assertThat(beanClasses).containsExactlyInAnyOrder(SampleController.class, SampleService.class, JdbcSampleRepository.class);
    }

    @Test
    @DisplayName("clear 시 모든 정보가 지워진다.")
    void clearTest() {
        beanFactory.clear();

        assertSoftly(softly -> {
            softly.assertThat(beanFactory.getBeanClasses()).isEmpty();
            softly.assertThat(beanFactory.getBean(SampleController.class)).isNull();
        });
    }

    @Test
    @DisplayName("Bean 생성 시 생성자에서 예외가 던져지면 실패한다.")
    void initializeFailWithExceptionTest() {
        final DefaultListableBeanFactory wrongBeanFactory = new DefaultListableBeanFactory(Set.of(ConstructorExceptionClass.class));

        assertThatThrownBy(wrongBeanFactory::initialize)
                .isInstanceOf(BeanInstantiationException.class)
                .rootCause()
                .isInstanceOf(IllegalStateException.class);
    }

    private static class ConstructorExceptionClass {
        @Autowired
        public ConstructorExceptionClass() {
            throw new IllegalStateException();
        }
    }

    @Test
    @DisplayName("구체 클래스가 아닌 클래스로 Bean 생성 시 예외가 던져지면 실패한다.")
    void initializeFailWithNotConcreteClassTest() {
        final DefaultListableBeanFactory wrongBeanFactory = new DefaultListableBeanFactory(Set.of(AbstractClass.class));

        assertThatThrownBy(wrongBeanFactory::initialize)
                .isInstanceOf(BeanInstantiationException.class)
                .rootCause()
                .isInstanceOf(InstantiationException.class);
    }

    private static abstract class AbstractClass {
        @Autowired
        public AbstractClass() {
        }
    }

    @Test
    @DisplayName("Bean 생성 시 생성자가 private 이면 실패한다.")
    void initializeFailWithPrivateTest() {
        final DefaultListableBeanFactory wrongBeanFactory = new DefaultListableBeanFactory(Set.of(PrivateConstructorClass.class));

        assertThatThrownBy(wrongBeanFactory::initialize)
                .isInstanceOf(BeanInstantiationException.class)
                .rootCause()
                .isInstanceOf(IllegalAccessException.class);
    }

    private static class PrivateConstructorClass {
        @Autowired
        private PrivateConstructorClass() {
        }
    }
}
