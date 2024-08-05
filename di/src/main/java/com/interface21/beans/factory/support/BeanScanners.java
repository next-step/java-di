package com.interface21.beans.factory.support;

import java.util.List;

public class BeanScanners implements BeanDefinitionScanner {

    private final List<BeanDefinitionScanner> beanDefinitionScanners;

    public BeanScanners(final Class<?> applicationClass) {
        final ConfigurationScanner configurationScanner = new ConfigurationScanner(List.of(applicationClass));
        final BeanScanner beanScanner = new BeanScanner(configurationScanner.getBasePackages());
        this.beanDefinitionScanners = List.of(configurationScanner, beanScanner);
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
