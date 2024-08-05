package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.ConfigurationBeanDefinition;
import com.interface21.beans.factory.config.SingletonBeanDefinition;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import com.interface21.context.annotation.Scope;
import com.interface21.context.stereotype.Controller;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultBeanDefinitionRegistryTest {

    @Test
    void beanDefinition을_받아_저장한다() {
        DefaultBeanDefinitionRegistry beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
        beanDefinitionRegistry.registerBeanDefinition(TestController.class, new SingletonBeanDefinition(TestController.class));
        assertThat(beanDefinitionRegistry.getBeanDefinitionMap()).containsKey("TestController");
    }

    @Test
    void Configuration을_저장하는_경우_하위_메소드빈도_함께_저장한다() {
        DefaultBeanDefinitionRegistry beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
        beanDefinitionRegistry.registerBeanDefinition(TestConfiguration.class, ConfigurationBeanDefinition.from(TestConfiguration.class));
        assertThat(beanDefinitionRegistry.getBeanDefinitionMap()).containsOnlyKeys("TestConfiguration", "testBean");
    }

    @Test
    void component이_아닌_클래스가_저장요청된_경우_예외가_발생한다() {
        DefaultBeanDefinitionRegistry registry = new DefaultBeanDefinitionRegistry();
        assertThatThrownBy(() -> registry.registerBeanDefinition(NoComponentController.class, new SingletonBeanDefinition(NoComponentController.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Component가 있는 클래스만 저장할 수 있습니다.");
    }

    @Test
    void 클래스에_맞는_BeanDefinition을_찾아_반환한다() {
        DefaultBeanDefinitionRegistry registry = new DefaultBeanDefinitionRegistry(Map.of("TestController", new SingletonBeanDefinition(TestController.class)));

        BeanDefinition actual = registry.getBeanDefinition(TestController.class);
        assertThat(actual.getType()).isEqualTo(TestController.class);
    }

    @Test
    void 존재하지않는_BeanDefinition을_찾으려는_경우_예외가_발생한다() {
        DefaultBeanDefinitionRegistry registry = new DefaultBeanDefinitionRegistry();
        assertThatThrownBy(() -> registry.getBeanDefinition(TestController.class))
                .isInstanceOf(BeanInstantiationException.class)
                .hasMessageContaining("생성할 수 있는 빈이 아닙니다.");
    }

    @Test
    void 존재하지_않는_beanName으로_BeanDefinition을_찾으려하는_경우_예외가_발생한다() {
        DefaultBeanDefinitionRegistry registry = new DefaultBeanDefinitionRegistry();
        assertThatThrownBy(() -> registry.getBeanDefinition("error"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 이름에 해당하는 BeanDefinition이 없습니다.");
    }

    @Test
    void beanName으로_BeanDefinition을_찾아_반환한다() {
        DefaultBeanDefinitionRegistry registry = new DefaultBeanDefinitionRegistry();
        registry.registerBeanDefinition(TestConfiguration.class, ConfigurationBeanDefinition.from(TestConfiguration.class));

        BeanDefinition actual = registry.getBeanDefinition("testBean");
        assertThat(actual.getType()).isEqualTo(String.class);
    }

    @Test
    void 모든_BeanDefinition을_반환한다() {
        SingletonBeanDefinition expectedBeanDefinition = new SingletonBeanDefinition(TestController.class);
        DefaultBeanDefinitionRegistry registry = new DefaultBeanDefinitionRegistry(Map.of("TestController", expectedBeanDefinition));

        List<BeanDefinition> actual = registry.getBeanDefinitions();
        assertThat(actual).containsExactly(expectedBeanDefinition);
    }

    @Controller
    public class TestController {
    }

    @Scope
    public class NoComponentController {
    }

    @Configuration
    public class TestConfiguration {
        @Bean
        public String testBean() {
            return "testBean";
        }
    }
}
