package com.interface21.beans.factory.config;

import com.interface21.context.annotation.Scope;
import com.interface21.context.stereotype.Component;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SingletonBeanDefinitionTest {

    @Test
    void Singleton_BeanDefinition을_생성한다() {
        SingletonBeanDefinition actual = new SingletonBeanDefinition(SingletonComponent.class);
        assertAll(
                () -> assertThat(actual.getType()).isEqualTo(SingletonComponent.class),
                () -> assertThat(actual.getBeanClassName())
                        .isEqualTo("com.interface21.beans.factory.config.SingletonBeanDefinitionTest$SingletonComponent"),
                () -> assertThat(actual.getScope()).isEqualTo(BeanScope.SINGLETON)
        );
    }

    @Test
    void Prototype을_SingletonBeanDefinition으로_생성하려하면_예외가_발생한다() {
        assertThatThrownBy(() -> new SingletonBeanDefinition(PrototypeComponent.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("싱글톤이 아닌 빈으로 생성할 수 없습니다.");
    }

    @Test
    void 전달된_클래스가_상위타입인지_확인한다() {
        boolean actual = new SingletonBeanDefinition(SingletonComponent.class).isAssignableTo(ComponentInterface.class);
        assertThat(actual).isTrue();
    }

    @Test
    void 전달된_클래스가_상위타입가_아닌지_확인한다() {
        boolean actual = new SingletonBeanDefinition(SingletonComponent2.class).isAssignableTo(ComponentInterface.class);
        assertThat(actual).isFalse();
    }

    public interface ComponentInterface {
    }

    @Component
    public class SingletonComponent implements ComponentInterface {
    }

    @Component
    public class SingletonComponent2 {
    }

    @Component
    @Scope(BeanScope.PROTOTYPE)
    public class PrototypeComponent {
    }
}
