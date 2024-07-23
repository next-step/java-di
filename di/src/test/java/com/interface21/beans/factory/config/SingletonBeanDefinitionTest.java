package com.interface21.beans.factory.config;

import com.interface21.context.stereotype.Component;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Component
    public class SingletonComponent {
    }
}
