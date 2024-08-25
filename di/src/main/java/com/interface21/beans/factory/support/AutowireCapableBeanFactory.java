package com.interface21.beans.factory.support;

import com.interface21.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {

    BeanInstantiationCache getBeanInstantiationCache();

}
