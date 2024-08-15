package com.interface21.beans.factory.support;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import com.interface21.beans.factory.config.BeanDefinition;

public class AnnotationBeanDefinitionReader implements BeanDefinitionReader {

    private final Logger log = LoggerFactory.getLogger(AnnotationBeanDefinitionReader.class);
    private final String[] basePackages;
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public AnnotationBeanDefinitionReader(String[] basePackages) {
        this.basePackages = basePackages;
        this.beanDefinitionRegistry = new AnnotaionBeanDefinitionRegistry();
    }

    @Override
    public void loadBeanDefinitions(Class<?>[] annotatedClasses) {
        var beansClasses = BeanScanner.scanBeans(basePackages);
        beansClasses.stream()
                .map(AnnotationBeanDefinition::new)
                .forEach(this::register);
    }

    private void register(AnnotationBeanDefinition beanDefinition) {

        beanDefinitionRegistry.registerBeanDefinition(beanDefinition.getType(), beanDefinition);

        log.info("BeanDefinition registered: {}", beanDefinition.getType().getSimpleName());
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionRegistry.getBeanClasses();
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitionRegistry.getBeanDefinitions();
    }
}
