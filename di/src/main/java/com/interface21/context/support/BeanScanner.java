package com.interface21.context.support;

import com.interface21.beans.factory.config.BeanDefinition;
import java.util.Map;

public interface BeanScanner {

    Map<Class<?>, BeanDefinition> scan();
}
