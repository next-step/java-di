package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import java.util.Arrays;
import java.util.Optional;

public interface Injector {
    Object inject(BeanDefinition beanDefinition, BeanFactory beanFactory);

    default Object[] resolveDependencies(BeanFactory beanFactory, Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
            .map(parameter -> Optional.ofNullable((Object) beanFactory.getBean(parameter))
                .orElseThrow(() -> new BeanInstantiationException(parameter, "argument not found"))
            )
            .toArray();
    }
}
