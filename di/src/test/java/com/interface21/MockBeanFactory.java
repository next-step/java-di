package com.interface21;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import com.interface21.beans.factory.support.BeanScanner;
import com.interface21.beans.factory.support.ClasspathBeanScanner;
import com.interface21.beans.factory.support.ConfigurationBeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.support.AnnotationConfigWebApplicationContext;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleService;
import samples.config.IntegrationConfig;

public class MockBeanFactory {

    public static BeanFactory createBeanFactory() {

        final var beanFactory = new DefaultListableBeanFactory();
        final var basePackages = BeanScanner.scanBasePackages(IntegrationConfig.class);

        new ConfigurationBeanScanner(beanFactory).scan(basePackages);
        new ClasspathBeanScanner(beanFactory).scan(basePackages);

        beanFactory.initialize();
        return beanFactory;
    }
}
