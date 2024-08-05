package com.interface21.beans.factory.support;

import java.util.List;

public class BeanScanner implements BeanDefinitionScanner {

    private final List<BeanDefinitionScanner> beanDefinitionScanners;

    public BeanScanner(final Class<?> applicationClass) {
        final ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(List.of(applicationClass));
        final ClassPathBeanScanner classPathBeanScanner = new ClassPathBeanScanner(configurationBeanScanner.getBasePackages());
        this.beanDefinitionScanners = List.of(configurationBeanScanner, classPathBeanScanner);
    }

    @Override
    public BeanDefinitionRegistry scan() {
        final BeanDefinitionRegistry resultRegistry = new SimpleBeanDefinitionRegistry();

        beanDefinitionScanners.stream()
                .map(BeanDefinitionScanner::scan)
                .forEach(resultRegistry::mergeBeanDefinitionRegistry);

        return resultRegistry;
    }
}
