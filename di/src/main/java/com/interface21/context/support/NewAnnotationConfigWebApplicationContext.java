package com.interface21.context.support;

import com.interface21.beans.factory.support.NewClassPathBeanScanner;
import com.interface21.beans.factory.support.NewConfigurationBeanScanner;
import com.interface21.beans.factory.support.NewDefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;

import java.util.Set;

public class NewAnnotationConfigWebApplicationContext implements ApplicationContext {

    private final NewDefaultListableBeanFactory beanFactory;

    public NewAnnotationConfigWebApplicationContext(final Class<?> applicationClass) {
        this.beanFactory = new NewDefaultListableBeanFactory();
        final NewConfigurationBeanScanner configurationBeanScanner = new NewConfigurationBeanScanner(beanFactory);
        configurationBeanScanner.register(applicationClass);

        final NewClassPathBeanScanner classPathBeanScanner = new NewClassPathBeanScanner(beanFactory);
        classPathBeanScanner.doScan(configurationBeanScanner.getBasePackages());
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
}
