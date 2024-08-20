package com.interface21.beans.factory.support;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import com.interface21.beans.factory.BeanFactory;

public class AutowiredConstructorArgumentResolver implements ArgumentResolver {

    private final Executable executable;
    private final BeanFactory beanFactory;

    public AutowiredConstructorArgumentResolver(Executable executable, BeanFactory beanFactory) {
        this.executable = executable;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object[] resolve() {
        return Arrays.stream(executable.getParameters())
                .map(Parameter::getType)
                .map(beanFactory::getBeanOrCreate)
                .toArray();
    }
}
