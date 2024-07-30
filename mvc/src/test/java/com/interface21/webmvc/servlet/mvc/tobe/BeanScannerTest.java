package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.SingletonBeanDefinition;
import com.interface21.beans.factory.support.BeanDefinitionRegistry;
import com.interface21.beans.factory.support.DefaultBeanDefinitionRegistry;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannerTest {

    @Test
    void Component연관_어노테이션을_스캔한다() {
        Map<Class<?>, BeanDefinition> actual = new BeanScanner().scanBean("samples");
        assertThat(actual).containsOnlyKeys(TestController.class);
    }

    @Test
    void BeanFactory에서_반환된_컨트롤러로_HandlerExecution을_스캔한다() {
        BeanDefinitionRegistry registry = new DefaultBeanDefinitionRegistry(Map.of("TestController", new SingletonBeanDefinition(TestController.class)));
        BeanFactory beanFactory = new DefaultListableBeanFactory(registry);
        beanFactory.initialize();

        Map<HandlerKey, HandlerExecution> actual = new BeanScanner().scan(beanFactory);
        assertThat(actual).containsOnlyKeys(
                new HandlerKey("/get-test", RequestMethod.GET),
                new HandlerKey("/post-test", RequestMethod.POST)
        );
    }
}
