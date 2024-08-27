package com.interface21.beans.factory.config;


import java.lang.reflect.Executable;

public interface BeanDefinition {

    Class<?> getType();

    Executable getExecutable();

    Class<?>[] getParameterTypes();

    AutowireStrategy autowireStrategy();
}
