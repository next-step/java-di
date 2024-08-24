package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.beans.factory.config.BeanDefinition;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.MyConfiguration;
import samples.SampleController;

class DefaultListableBeanFactoryTest {

    private DefaultListableBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        final Class<MyConfiguration> myConfigurationClass = MyConfiguration.class;
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(myConfigurationClass);
        final Map<Class<?>, BeanDefinition> configurationBeanDefinitionMap = new ConfigurationBeanScanner(myConfigurationClass).scan();
        final Map<Class<?>, BeanDefinition> genericBeanDefinitionMap = new BeanScanner(configurationBeanScanner.getBasePackages()).scan();

        final SimpleBeanDefinitionRegistry simpleBeanDefinitionRegistry = new SimpleBeanDefinitionRegistry();
        simpleBeanDefinitionRegistry.registerAll(configurationBeanDefinitionMap, genericBeanDefinitionMap);

        this.beanFactory = new DefaultListableBeanFactory(simpleBeanDefinitionRegistry);
        this.beanFactory.initialize();
    }

    @Test
    public void di() {
        final var sampleController = beanFactory.getBean(SampleController.class);
        final var sampleService = sampleController.getSampleService();

        assertAll(
            () -> assertThat(sampleController).isNotNull(),
            () -> assertThat(sampleController.getSampleService()).isNotNull(),
            () -> assertThat(sampleService.getSampleRepository()).isNotNull()
        );
    }

    @Test
    @DisplayName("@Controller 가 설정된 빈 목록들을 조회한다")
    void getControllersTest() {
        Map<Class<?>, Object> controllers = beanFactory.getControllers();

        assertAll(
            () -> assertThat(controllers).isNotNull(),
            () -> assertThat(controllers).hasSize(1),
            () -> assertThat(controllers.containsKey(SampleController.class)).isTrue()
        );
    }
}
