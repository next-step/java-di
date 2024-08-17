package com.interface21.beans.factory.support.injector;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.BeanFactoryUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ConfigurationInjector implements InjectorConsumer<Method> {
    private final Method method;
    private final Class<?> beanClazz;

    public ConfigurationInjector(Method method) {
        this.method = method;
        this.beanClazz = method.getReturnType();
    }

    @Override
    public boolean support() {
        return method != null;
    }

    @Override
    public Object inject(BeanFactory beanFactory) {
        Object[] params = Arrays.stream(method.getParameterTypes())
                .map(param -> beanFactory.getBean(param))
                .toArray();
        Object returnedBean = BeanFactoryUtils.invokeMethod(method, beanClazz, params);

        return returnedBean;
    }
}
