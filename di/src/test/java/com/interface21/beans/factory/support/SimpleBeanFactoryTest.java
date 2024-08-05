package com.interface21.beans.factory.support;

import com.interface21.beans.NoSuchBeanDefinitionException;
import com.interface21.context.stereotype.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class SimpleBeanFactoryTest {

    private SimpleBeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        beanFactory = new SimpleBeanFactory();
    }

    @Test
    @DisplayName("등록한 객체를 조회하면 동일한 객체가 조회된다.")
    void addAndGetTest() {
        final FirstTestBean firstTestBean = new FirstTestBean();
        beanFactory.addBean("firstTestBean", firstTestBean);

        final FirstTestBean result = beanFactory.getBean(FirstTestBean.class);

        assertThat(firstTestBean).isSameAs(result);
    }

    @Test
    @DisplayName("등록한 Bean 정보들을 조회할 수 있다.")
    void getBeanClassesTest() {
        beanFactory.addBean("firstTestBean", new FirstTestBean());
        beanFactory.addBean("secondTestBean", new SecondTestBean());

        final Set<Class<?>> beanClasses = beanFactory.getBeanClasses();

        assertThat(beanClasses).containsExactlyInAnyOrder(FirstTestBean.class, SecondTestBean.class);
    }

    @Test
    @DisplayName("이미 등록된 이름의 Bean 을 다시 등록하면 예외가 던져진다.")
    void addBeanNameAlreadyExistTest() {
        beanFactory.addBean("firstTestBean", new FirstTestBean());

        assertThatThrownBy(() -> beanFactory.addBean("firstTestBean", new SecondTestBean()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되어 있지 않은 Bean 을 조회하면 예외를 던진다.")
    void beanNotAddedTest() {
        assertThatThrownBy(() -> beanFactory.getBean(FirstTestBean.class))
                .isInstanceOf(NoSuchBeanDefinitionException.class);
    }

    @Test
    @DisplayName("Bean 을 전부 비울 수 있다.")
    void clearTest() {
        final FirstTestBean firstTestBean = new FirstTestBean();
        beanFactory.addBean("firstTestBean", firstTestBean);

        beanFactory.clear();

        assertSoftly(softly -> {
            assertThat(beanFactory.getBeanClasses()).isEmpty();
            assertThatThrownBy(() -> beanFactory.getBean(FirstTestBean.class))
                    .isInstanceOf(NoSuchBeanDefinitionException.class);
        });
    }

    @Test
    @DisplayName("해당 클래스의 Bean 이 등록되어 있는지 여부를 반환받을 수 있다.")
    void containsBeanTest() {
        final FirstTestBean firstTestBean = new FirstTestBean();
        beanFactory.addBean("firstTestBean", firstTestBean);

        assertThat(beanFactory.containsBean("firstTestBean")).isTrue();
    }

    public static class FirstTestBean {
    }

    public static class SecondTestBean {
    }

    @Controller
    public static class ControllerBean {
    }

}
