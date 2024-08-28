package com.interface21.context.support;

import com.interface21.beans.factory.support.ClassPathBeanScanner;
import com.interface21.beans.factory.support.ComponentScanBasePackageResolver;
import com.interface21.beans.factory.support.ConfigurationBeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.beans.factory.support.SimpleBeanDefinitionRegistry;
import com.interface21.context.ApplicationContext;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(Class<?>... configurationClasses) {
        final List<String> basePackages = ComponentScanBasePackageResolver.getBasePackages(configurationClasses);
        SimpleBeanDefinitionRegistry beanDefinitionRegistry = new SimpleBeanDefinitionRegistry();
        new ConfigurationBeanScanner(beanDefinitionRegistry).scan(basePackages);
        new ClassPathBeanScanner(beanDefinitionRegistry).scan(basePackages);

        this.beanFactory = new DefaultListableBeanFactory(beanDefinitionRegistry);
        this.beanFactory.initialize();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }

    @Override
    public Map<Class<?>, Object> getControllers() {
        return beanFactory.getControllers();
    }
}
