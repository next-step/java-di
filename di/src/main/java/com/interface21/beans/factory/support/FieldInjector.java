package com.interface21.beans.factory.support;

import com.interface21.beans.BeanUtils;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import java.lang.reflect.Field;
import java.util.Set;

public class FieldInjector implements Injector {
    @Override
    public Object inject(BeanDefinition beanDefinition, BeanFactory beanFactory) {
        Object bean = BeanUtils.instantiate(beanDefinition.getType());
        Set<Field> fields = beanDefinition.getFields();

        for (Field field : fields) {
            Object injectedBean = beanFactory.getBean(field.getType());
            BeanUtils.setField(field, bean, injectedBean);
        }

        return bean;
    }
}
