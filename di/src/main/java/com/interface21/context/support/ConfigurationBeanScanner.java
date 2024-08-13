package com.interface21.context.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.beancreator.ConfigurationClassBeanInstantiation;
import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import com.interface21.core.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ConfigurationBeanScanner implements BeanScanner {
    public static final Class<Configuration> CONFIGURATION_ANNOTATION = Configuration.class;

    private final BeanFactory beanFactory;

    private String[] basePackages;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register(Class<?> initialConfigurationClass) {
        collectBasePackages(initialConfigurationClass.getPackageName());

        registerBeanInstantiations();
    }

    private void collectBasePackages(String... initialBasePackage) {
        Set<Class<?>> configurations = ReflectionUtils.getTypesAnnotatedWith(initialBasePackage, CONFIGURATION_ANNOTATION);
        Stream<String> additionalBasePackages = configurations
                .stream()
                .filter(configuration -> configuration.isAnnotationPresent(ComponentScan.class))
                .flatMap(configuration -> Arrays.stream(configuration.getAnnotation(ComponentScan.class).value()))
                .distinct();

        basePackages = Stream.concat(
                Arrays.stream(initialBasePackage),
                additionalBasePackages
        ).toArray(String[]::new);
    }

    @Override
    public void registerBeanInstantiations() {
        scanConfigurationBeans().forEach(method ->
                beanFactory.registerBeanInstantiation(
                        method.method().getReturnType(),
                        new ConfigurationClassBeanInstantiation(method.object(), method.method()))
        );
    }

    /* visible for testing */
    String[] getBasePackages() {
        return basePackages;
    }

    /* visible for testing */
    List<ConfigurationInstanceAndMethod> scanConfigurationBeans() {
        Set<Class<?>> configurations = ReflectionUtils.getTypesAnnotatedWith(basePackages, CONFIGURATION_ANNOTATION);
        return configurations
                .stream()
                .flatMap(configurationClass -> {
                    Object object = BeanUtils.instantiate(configurationClass);
                    Method[] declaredMethods = configurationClass.getDeclaredMethods();

                    return Arrays.stream(declaredMethods)
                                 .filter(this::isBeanMethod)
                                 .map(method -> new ConfigurationInstanceAndMethod(object, method));
                })
                .toList();
    }

    private boolean isBeanMethod(final Method beanMethod) {
        return beanMethod.isAnnotationPresent(Bean.class) &&
                Modifier.isPublic(beanMethod.getModifiers());
    }
}
