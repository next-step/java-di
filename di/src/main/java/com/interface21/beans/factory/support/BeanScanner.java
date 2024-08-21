package com.interface21.beans.factory.support;

import com.interface21.beans.factory.config.BeanDefinitions;

import java.util.List;

public class BeanScanner implements BeanDefinitionScanner {
    private final List<BeanDefinitionScanner> beanDefinitionScanners;

    public BeanScanner(final Class<?> applicationClass) {
        final ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(List.of(applicationClass));
        ClassPathBeanScanner classPathBeanScanner = new ClassPathBeanScanner(configurationBeanScanner.getBasePackages());
        this.beanDefinitionScanners = List.of(configurationBeanScanner, classPathBeanScanner);
    }

    public BeanDefinitions scan() {
        final BeanDefinitions beanDefinitions = new BeanDefinitions();
        beanDefinitionScanners.stream()
                .map(BeanDefinitionScanner::scan)
                .forEach(beanDefinitions::mergeBeanDefinitions);
        return beanDefinitions;
    }
}
