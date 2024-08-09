package com.interface21.context.support.tobe;

import com.interface21.context.ApplicationContext;

import java.util.Set;

public class BeanScanner {
    private final ApplicationContext applicationContext;

    public BeanScanner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Set<Class<?>> scan() {
        return applicationContext.getBeanClasses();
    }
}
