package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.context.support.AnnotatedBeanDefinition;
import java.lang.reflect.Method;

public class ConfigurationInjector implements Injector {
    @Override
    public Object inject(BeanDefinition beanDefinition, BeanFactory beanFactory) {
        Method method = ((AnnotatedBeanDefinition) beanDefinition).getMethod();
        Object bean = beanFactory.getBean(method.getDeclaringClass());
        Object[] args = resolveDependencies(beanFactory, method.getParameterTypes());
        return BeanFactoryUtils.invokeMethod(method, bean, args).get();
    }
}
