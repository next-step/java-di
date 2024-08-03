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

    private final BeanDefinitions beanDefinitions;
    private final Beans beans;
    private final CircularReferenceSensor circularReferenceSensor;

    public DefaultListableBeanFactory() {
        this.beanDefinitions = new BeanDefinitions();
        this.beans = new Beans();
        this.circularReferenceSensor = new CircularReferenceSensor();
    }

    public void initialize() {
        log.info("Start DefaultListableBeanFactory");

        BasePackageScanner basePackageScanner = new BasePackageScanner();
        BeanScanner beanScanner = new BeanScanner(basePackageScanner.scan());

        Set<Class<?>> componentBeanClasses = beanScanner.scanComponent();
        beanDefinitions.registerComponentBeanDefinitions(componentBeanClasses);

        Set<Class<?>> configurationClasses = beanScanner.scanConfiguration();
        beanDefinitions.registerConfigurationBeanDefinitions(configurationClasses);
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
            Constructor<?> constructor = findAutoWiredConstructor(componentBeanDefinition.getType());
            List<? extends Class<?>> parameterTypes = extractParameterTypes(constructor);
            Object[] constructorArgs = instantiateAll(parameterTypes);
            return BeanUtils.instantiateClass(constructor, constructorArgs);
        }

        if (beanDefinition instanceof ConfigurationBeanDefinition configurationBeanDefinition) {
            Method creationMethod = configurationBeanDefinition.getCreationMethod();
            List<? extends Class<?>> parameterTypes = extractParameterTypes(creationMethod);
            Object[] methodParameters = instantiateAll(parameterTypes);
            try {
                return creationMethod.invoke(configurationBeanDefinition.getConfigurationObject(), methodParameters);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        throw new IllegalArgumentException("빈으로 생성할 수 없는 타입입니다. type=%s".formatted(beanDefinition.getType()));
    }

    private List<? extends Class<?>> extractParameterTypes(Method creationMethod) {
        return Arrays.stream(creationMethod.getParameters())
                .map(Parameter::getType)
                .toList();
    }

    private Constructor<?> findAutoWiredConstructor(Class<?> clazz) {
        Constructor<?> autowiredConstructor = BeanFactoryUtils.getAutowiredConstructor(clazz);
        if (autowiredConstructor == null) {
            return clazz.getDeclaredConstructors()[0];
        }
        return autowiredConstructor;
    }

    private Object[] instantiateAll(List<? extends Class<?>> types) {
        detectCirculation(types);
        return types.stream()
                .map(this::getBean)
                .toArray();
    }

    private List<? extends Class<?>> extractParameterTypes(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameters())
                .map(Parameter::getType)
                .toList();
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
