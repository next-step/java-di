package com.interface21;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import samples.JdbcSampleRepository;
import samples.SampleController;
import samples.SampleDataSource;
import samples.SampleService;

public class MockBeanFactory {

    public static BeanFactory createBeanFactory() {

        final var factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition(
                SampleController.class, new AnnotationBeanDefinition(SampleController.class));
        factory.registerBeanDefinition(
                SampleService.class, new AnnotationBeanDefinition(SampleService.class));
        factory.registerBeanDefinition(
                JdbcSampleRepository.class,
                new AnnotationBeanDefinition(JdbcSampleRepository.class));
        factory.registerBeanDefinition(
                SampleDataSource.class, new AnnotationBeanDefinition(SampleDataSource.class));

        factory.initialize();

        return factory;
    }
}
