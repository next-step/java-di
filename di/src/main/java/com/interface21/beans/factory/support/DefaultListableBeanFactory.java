package com.interface21.beans.factory.support;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.config.AnnotationBeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    private final Map<Class<?>, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private final BeanRegistry beanRegistry;
    private final Set<Class<?>> initializingBeans = ConcurrentHashMap.newKeySet();
    private final ConstructorArgumentValueHolder constructorArgumentValueHolder;

    public DefaultListableBeanFactory() {
        this.beanRegistry = new DefaultBeanRegistry();
        this.constructorArgumentValueHolder = new ConstructorArgumentValueHolder();
    }

    public void initialize() {
        initializeBeans();
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(clazz, beanDefinition);
        log.info("BeanDefinition registered for {}", clazz.getSimpleName());
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.keySet();
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return List.copyOf(beanDefinitionMap.values());
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanDefinitions().size();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        Object bean = beanRegistry.getBean(clazz);
        if (bean != null) {
            return clazz.cast(bean);
        }
        return clazz.cast(instantiateClass(clazz));
    }

    @Override
    public void clear() {}

    @Override
    public Object[] registerArgumentValues(Class<?> type, Class<?>[] parameterTypes) {
        Object[] args = Arrays.stream(parameterTypes).map(this::getBean).toArray(Object[]::new);
        return constructorArgumentValueHolder.addValueHolder(type, args);
    }

    private void initializeBeans() {
        beanDefinitionMap
                .values()
                .forEach(
                        beanDefinition -> {
                            Object instance = createBean((AnnotationBeanDefinition) beanDefinition);
                            beanRegistry.registeredBean(instance);
                        });
    }

    private Object createBean(AnnotationBeanDefinition beanDefinition) {
        if (!beanDefinition.isAutowireMode()) {
            return BeanUtils.instantiate(beanDefinition.getType());
        }

        return new ConstructorResolver(this).autowireConstructor(beanDefinition);
    }

    private Object instantiateClass(Class<?> clazz) {
        Class<?> concreteClazz =
                BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses())
                        .orElseThrow(() -> new BeanClassNotFoundException(clazz.getSimpleName()));

        AnnotationBeanDefinition beanDefinition = (AnnotationBeanDefinition) beanDefinitionMap.get(concreteClazz);
        if (beanDefinition == null) {
            throw new BeanInstantiationException(clazz, "No BeanDefinition found for " + clazz.getSimpleName());
        }

        Constructor<?> constructor = beanDefinition.getConstructor();
        Object[] args = registerArgumentValues(concreteClazz, constructor.getParameterTypes());

        return instantiateBean(constructor, args);
    }


    private Object instantiateBean(Constructor<?> constructor, Object[] args) {
        Object instance = BeanUtils.instantiateClass(constructor, args);
        initializingBeans.remove(constructor.getDeclaringClass());
        return instance;
    }
}
