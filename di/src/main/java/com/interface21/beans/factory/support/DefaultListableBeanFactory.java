package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.SingletonBeanDefinition;
import com.interface21.beans.factory.exception.DependenciesAreTooDeep;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import com.interface21.core.util.ReflectionUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private static final int BEAN_CREATION_LOOP_TRIAL = 20;

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final String[] basePackages;

    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();

    public DefaultListableBeanFactory(String[] basePackages) {
        this.basePackages = basePackages;
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        Set<Class<?>> classes =
                ReflectionUtils.getTypesAnnotatedWith(basePackages, Repository.class, Service.class, Controller.class);

        classes.forEach(this::registerBeanDefinition);
        registerAllBeans(classes, 0);
    }

    private void registerAllBeans(Collection<Class<?>> classes, int depth) {
        // NOTE: 빈 생성시 의존관계 체크 - 가장 간단한 방법으로 막아 두었습니다.
        if (depth > BEAN_CREATION_LOOP_TRIAL && !classes.isEmpty()) {
            throw new DependenciesAreTooDeep(classes);
        }

        List<Class<?>> failedClasses =
                classes.stream()
                       .filter(clazz -> !register(clazz))
                       .toList();

        if (failedClasses.isEmpty()) {
            return;
        }

        registerAllBeans(failedClasses, depth + 1);
    }

    /**
     * 빈 생성해서 singletonObjects 에 등록한다.
     *
     * @param clazz: 생성할 빈 클래스
     * @return 생성에 실패한 경우 false, 생성에 성공했거나, 성공했던 객체의 경우 true
     */
    private boolean register(Class<?> clazz) {
        if (singletonObjects.containsKey(clazz)) {
            return true;
        }

        Object object = instantiate(clazz);
        if (object == null) {
            return false;
        }

        singletonObjects.put(clazz, object);
        for (Class<?> anInterface : clazz.getInterfaces()) {
            singletonObjects.put(anInterface, object);
        }
        return true;
    }

    /**
     * 입력받은 클래스의 빈을 생성하여 돌려준다.
     *
     * @return 생성한 오브젝트. 주입할 빈을 못 찾은 경우 null 리턴
     */
    @Nullable
    private Object instantiate(Class<?> clazz) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor == null) {
            return instantiateWithDefaultConstructor(clazz);
        } else {
            return instantiateWithInjectedConstructor(injectedConstructor);
        }
    }

    /**
     * 기본 생성자로 객체를 생성한다
     */
    private static Object instantiateWithDefaultConstructor(Class<?> clazz) {
        return BeanUtils.instantiate(clazz);
    }

    /**
     * injectedConstructor 생성자로 객체를 생성한다.
     * 필요한 파라메터를 singletonObjects 에서 가져오는데, 충분하지 않을 경우 객체 생성에 실패하고 null 을 리턴한다.
     */
    @Nullable
    private Object instantiateWithInjectedConstructor(Constructor<?> injectedConstructor) {
        Parameter[] parameters = injectedConstructor.getParameters();
        Object[] arguments = getArgumentsFromSingletonObjects(parameters);
        if (Arrays.stream(arguments).anyMatch(Objects::isNull)) {
            return null;
        }

        return BeanUtils.instantiateClass(injectedConstructor, arguments);
    }

    /**
     * injectedConstructor 생성자로 객체를 생성할 때 필요한 파라메터를 구해낸다
     */
    private Object[] getArgumentsFromSingletonObjects(Parameter[] parameters) {
        return Arrays.stream(parameters)
                     .map(parameter -> singletonObjects.get((parameter.getType())))
                     .toArray();
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return singletonObjects.keySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        return (T) singletonObjects.get(clazz);
    }

    @Override
    public void clear() {
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(clazz.getName(), beanDefinition);
    }

    private void registerBeanDefinition(Class<?> clazz) {
        registerBeanDefinition(clazz, new SingletonBeanDefinition(clazz));
    }
}
