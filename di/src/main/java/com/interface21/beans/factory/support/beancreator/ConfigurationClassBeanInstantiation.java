package com.interface21.beans.factory.support.beancreator;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.BeanInstantiation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Configuration 의 Bean 이 달려있는 메서드로 빈을 생성하도록 도와주는 객체
 */
public class ConfigurationClassBeanInstantiation implements BeanInstantiation {
    private final Object object;
    private final Class<?> clazz;
    private final Method method;

    public ConfigurationClassBeanInstantiation(final Object object, final Method method) {
        this.object = object;
        this.clazz = method.getReturnType();
        this.method = method;
    }

    @Override
    public Object instantiateClass(final BeanFactory beanFactory) {
        try {
            return method.invoke(object,
                    getArgumentsFromSingletonObjects(method.getParameters(), beanFactory)
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }
}
