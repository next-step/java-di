package com.interface21.beans.factory.config;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.support.BeanDefinitionReader;
import com.interface21.beans.factory.support.BeanDefinitionRegistry;
import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.context.annotation.Bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ConfigurationClassBeanDefinitionReader implements BeanDefinitionReader {
    private final BeanDefinitionRegistry registry;

    public ConfigurationClassBeanDefinitionReader(final BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void loadBeanDefinitions(final Class<?>... annotatedClasses) {
        Arrays.stream(annotatedClasses).forEach(this::loadBeanDefinition);
    }

    private void loadBeanDefinition(final Class<?> annotatedClass) {
        BeanFactoryUtils.getBeanMethods(annotatedClass, Bean.class)
                .forEach(beanMethod -> loadBeanDefinitionsForBeanMethod(beanMethod, annotatedClass));
    }

    private void loadBeanDefinitionsForBeanMethod(final Method beanMethod, final Class<?> factoryClassType) {
        final ConfigurationClassBeanDefinition beanDefinition = new ConfigurationClassBeanDefinition(beanMethod, factoryClassType);
        registry.registerBeanDefinition(beanDefinition.type, beanDefinition);
    }

    public void register(final Class<?> annotatedClass) {
        registry.registerBeanDefinition(annotatedClass, new RootBeanDefinition(annotatedClass, annotatedClass.getName()));
        loadBeanDefinitions(annotatedClass);
    }

    private static class ConfigurationClassBeanDefinition implements BeanDefinition {

        private final Class<?> type;
        private final String beanClassName;
        private final Class<?> factoryBeanType;
        private final Method factoryMethod;

        public ConfigurationClassBeanDefinition(final Method method, final Class<?> factoryBeanType) {
            final Class<?> beanType = method.getReturnType();
            this.type = beanType;
            this.beanClassName = beanType.getName();
            this.factoryBeanType = factoryBeanType;
            this.factoryMethod = method;
        }

        @Override
        public Class<?> getType() {
            return this.type;
        }

        @Override
        public String getBeanClassName() {
            return beanClassName;
        }

        @Override
        public Object createInstance(final BeanGetter beanGetter) {
            return createByBeanMethod(beanGetter);
        }

        private Object createByBeanMethod(final BeanGetter beanGetter) {
            try {
                final Object[] parameters = Arrays.stream(factoryMethod.getParameterTypes())
                        .map(beanGetter::getBean)
                        .toArray();

                factoryMethod.setAccessible(true);

                return factoryMethod.invoke(beanGetter.getBean(factoryBeanType), parameters);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BeanInstantiationException(type, "Failed to instantiate bean", e);
            } finally {
                factoryMethod.setAccessible(false);
            }
        }
    }
}
