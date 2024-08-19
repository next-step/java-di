package com.interface21.beans.factory.support.injector;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ConstructorInjector implements InjectorConsumer<Constructor<?>> {

    private final Constructor<?> constructor;

    public ConstructorInjector(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public boolean support() {
        return constructor != null && constructor.isAnnotationPresent(Autowired.class);
    }

    @Override
    public Object inject(BeanFactory beanFactory) {
        Object[] params = Arrays.stream(constructor.getParameterTypes())
            .map(param -> beanFactory.getBean(param))
            .toArray();
        Object bean = BeanUtils.instantiateClass(constructor, params);
        return bean;
    }


}
