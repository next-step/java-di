package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.support.BeanDefinitionRegistry;
import com.interface21.beans.factory.support.DefaultBeanDefinitionRegistry;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;

import java.util.List;
import java.util.Map;

public class ApplicationContext {

    private final Object[] basePackage;
    private final BeanScanner beanScanner;
    private final ConfigurationScanner configurationScanner;
    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private final BeanFactory beanFactory;

    public ApplicationContext(List<Class<?>> configurations, Object... basePackage) {
        this.basePackage = basePackage;
        this.beanScanner = new BeanScanner();
        this.configurationScanner = new ConfigurationScanner(configurations);
        this.beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
        this.beanFactory = new DefaultListableBeanFactory(this.beanDefinitionRegistry);
    }

    public Map<HandlerKey, HandlerExecution> scan() {
        Map<Class<?>, BeanDefinition> beanDefinitions = beanScanner.scanBean(basePackage);
        beanDefinitions.putAll(configurationScanner.scanBean());
        for (Map.Entry<Class<?>, BeanDefinition> beanDefinitionEntry : beanDefinitions.entrySet()) {
            beanDefinitionRegistry.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
        }
        beanFactory.initialize();
        return beanScanner.scan(beanFactory);
    }
}
