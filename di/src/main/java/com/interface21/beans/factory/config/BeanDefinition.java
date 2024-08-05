package com.interface21.beans.factory.config;

import com.interface21.beans.factory.support.InjectMode;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public interface BeanDefinition {

    Class<?> getType();

    String getBeanClassName();

    InjectMode getInjectMode();

    Constructor<?> getConstructor();

    Set<Method> getMethods();

    Set<Field> getFields();
}
