package com.interface21.beans.factory.support.injector;

import com.interface21.beans.factory.BeanFactory;

public interface InjectorConsumer<T> {

    boolean support();

    Object inject(BeanFactory beanFactory);
}
