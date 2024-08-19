package com.interface21.beans.factory.support.injector;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.core.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class ConfigurationInjector implements InjectorConsumer<Method> {

    private final Method method;
    private final Class<?> beanClazz;
    private final Class<?> configClazz;

    public ConfigurationInjector(Method method) {
        this.method = method;
        this.beanClazz = method.getReturnType();
        this.configClazz = method.getDeclaringClass();
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
        Object bean = ReflectionUtils.newInstance(configClazz);
        Optional<Object> returnedBean = BeanFactoryUtils.invokeMethod(method, bean, params);

        return returnedBean.get();
    }
}
