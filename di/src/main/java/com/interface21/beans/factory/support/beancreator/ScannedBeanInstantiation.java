package com.interface21.beans.factory.support.beancreator;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.beans.factory.support.BeanInstantiation;

import java.lang.reflect.Constructor;

/**
 * 어노테이션 방식으로 등록된 빈을 생성할 수 있게 도와주는 객체
 */
public class ScannedBeanInstantiation implements BeanInstantiation {
    private final Class<?> clazz;
    private final Constructor<?> constructor;

    public ScannedBeanInstantiation(final Class<?> clazz) {
        assert !clazz.isInterface();

        this.clazz = clazz;
        this.constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
    }

    @Override
    public Object instantiateClass(final BeanFactory beanFactory) {
        if (constructor == null) {
            return BeanUtils.instantiate(clazz);
        }

        return BeanUtils.instantiateClass(
                constructor,
                getArgumentsFromSingletonObjects(constructor.getParameters(), beanFactory)
        );
    }

    @Override
    public Class<?> getClazz() {
        return null;
    }
}
