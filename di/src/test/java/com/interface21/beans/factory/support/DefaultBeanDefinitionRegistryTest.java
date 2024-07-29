package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.SingletonBeanDefinition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultBeanDefinitionRegistryTest {

    private final DefaultBeanDefinitionRegistry beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();

    @Test
    void beanDefinition을_받아_저장한다() {
        beanDefinitionRegistry.registerBeanDefinition(DefaultListableBeanFactoryTest.TestController.class, new SingletonBeanDefinition(DefaultListableBeanFactoryTest.TestController.class));
        assertThat(beanDefinitionRegistry.getBeanDefinitionMap()).containsKey("TestController");
    }

    @Test
    void component이_아닌_클래스가_저장요청된_경우_예외가_발생한다() {
        assertThatThrownBy(() -> beanDefinitionRegistry.registerBeanDefinition(DefaultListableBeanFactoryTest.NoComponentController.class, new SingletonBeanDefinition(DefaultListableBeanFactoryTest.NoComponentController.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Component가 있는 클래스만 저장할 수 있습니다.");
    }
}
