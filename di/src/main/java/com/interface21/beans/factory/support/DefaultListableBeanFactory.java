package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Class<?> startClass;
    private final BeanDefinitions beanDefinitions;
    private final Beans beans;
    private final CircularReferenceSensor circularReferenceSensor;

    public DefaultListableBeanFactory(Class<?> startClass) {
        this.startClass = startClass;
        this.beanDefinitions = new BeanDefinitions();
        this.beans = new Beans();
        this.circularReferenceSensor = new CircularReferenceSensor();
    }

    public void initialize() {
        log.info("Start DefaultListableBeanFactory");

        BasePackageScanner basePackageScanner = new BasePackageScanner(startClass);
        String[] basePackages = basePackageScanner.scan();

        ClassPathBeanScanner classPathBeanScanner = new ClassPathBeanScanner(basePackages);
        classPathBeanScanner.registerBeanDefinitions(beanDefinitions);

        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(basePackages);
        configurationBeanScanner.registerBeanDefinitions(beanDefinitions);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beans.getBeanClasses();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(final Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Null이 들어올 수 없습니다.");
        }

        if (beans.isRegistered(clazz)) {
            return beans.getBean(clazz);
        }

        T bean = (T) createBean(clazz);
        circularReferenceSensor.removeAllTargets();
        return bean;
    }

    private Object createBean(Class<?> clazz) {
        circularReferenceSensor.addTarget(clazz);

        BeanDefinition beanDefinition = beanDefinitions.getByType(clazz);
        Object newBean = createBean(beanDefinition);
        beans.register(newBean);

        return newBean;
    }

    public Object createBean(BeanDefinition beanDefinition) {
        if (beanDefinition instanceof ComponentBeanDefinition componentBeanDefinition) {
            Constructor<?> constructor = componentBeanDefinition.getAutoWiredConstructor();
            Object[] constructorArgs = instantiateAllParameters(constructor.getParameters());
            return BeanUtils.instantiateClass(constructor, constructorArgs);
        }

        if (beanDefinition instanceof ConfigurationBeanDefinition configurationBeanDefinition) {
            Method creationMethod = configurationBeanDefinition.getCreationMethod();
            Object[] methodParameters = instantiateAllParameters(creationMethod.getParameters());
            try {
                return creationMethod.invoke(configurationBeanDefinition.getConfigurationObject(), methodParameters);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        throw new IllegalArgumentException("빈으로 생성할 수 없는 타입입니다. type=%s".formatted(beanDefinition.getType()));
    }

    private List<? extends Class<?>> extractParameterTypes(Parameter[] parameters) {
        return Arrays.stream(parameters)
                .map(Parameter::getType)
                .toList();
    }

    private Object[] instantiateAllParameters(Parameter[] parameters) {
        List<? extends Class<?>> types = extractParameterTypes(parameters);
        detectCirculation(types);

        return types.stream()
                .map(this::getBean)
                .toArray();
    }

    private void detectCirculation(List<? extends Class<?>> types) {
        types.forEach(circularReferenceSensor::detect);
    }

    @Override
    public void clear() {
        beans.clear();
    }

    @Override
    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotationType) {
        return beanDefinitions.extractTypes()
                .stream()
                .filter(type -> type.isAnnotationPresent(annotationType))
                .collect(Collectors.toUnmodifiableMap(type -> type, this::getBean));
    }
}
