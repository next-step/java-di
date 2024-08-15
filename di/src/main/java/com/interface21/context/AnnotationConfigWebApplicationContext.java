package com.interface21.context;

import com.interface21.beans.BeansCache;
import com.interface21.beans.factory.support.ClasspathBeanScanner;
import com.interface21.beans.factory.support.ConfigurationBeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;

import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;
    private final Class<?> configurationClass;

    public AnnotationConfigWebApplicationContext(Class<?> configurationClass) {
        this.configurationClass = configurationClass;
        this.beanFactory = new DefaultListableBeanFactory();
    }

    @Override
    public void initialize() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(beanFactory);
        configurationBeanScanner.register(configurationClass);

        ClasspathBeanScanner cbs = new ClasspathBeanScanner(beanFactory);
        cbs.doScan(configurationBeanScanner.getBasePackages());

        beanFactory.initialize();
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
    public BeansCache getControllers() {
        return beanFactory.getControllers();
    }
}
