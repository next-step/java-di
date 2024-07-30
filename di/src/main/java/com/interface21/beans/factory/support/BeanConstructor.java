package com.interface21.beans.factory.support;

import com.interface21.beans.factory.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BeanConstructor {

    private static final Random RANDOM = new Random();
    private static final int NOARG_CONSTRUCTOR_PARAMETER_COUNT = 0;

    private final Constructor<?> constructor;
    private final List<Class<?>> parameterTypes;
    private final boolean isNoArgument;

    public BeanConstructor(Constructor<?> constructor) {
        List<Class<?>> parameterTypes = Arrays.stream(constructor.getParameterTypes())
                .toList();
        this.constructor = constructor;
        this.parameterTypes = parameterTypes;
        this.isNoArgument = parameterTypes.isEmpty();
    }

    public static BeanConstructor createTargetConstructor(Class<?> clazz) {
        List<Constructor<?>> constructors = Arrays.stream(clazz.getConstructors())
                .toList();

        return constructors.stream()
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .findAny()
                .map(BeanConstructor::new)
                .orElseGet(() -> createNormalBeanConstructor(constructors));
    }

    private static BeanConstructor createNormalBeanConstructor(List<Constructor<?>> constructors) {
        return constructors.stream()
                .filter(BeanConstructor::isNoArgConstructor)
                .findAny()
                .map(BeanConstructor::new)
                .orElseGet(() -> randomConstructor(constructors));
    }

    private static boolean isNoArgConstructor(Constructor<?> constructor) {
        return constructor.getParameterCount() == NOARG_CONSTRUCTOR_PARAMETER_COUNT;
    }

    private static BeanConstructor randomConstructor(List<Constructor<?>> constructors) {
        return new BeanConstructor(constructors.get(RANDOM.nextInt(constructors.size())));
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public List<Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    public boolean isNoArgument() {
        return isNoArgument;
    }
}
