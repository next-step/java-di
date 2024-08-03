package com.interface21.web.context.support;

import com.interface21.beans.factory.BeanFactory;
import jakarta.servlet.ServletContext;

public abstract class BeanFactoryUtils {
    public static BeanFactory getBeanFactory(final ServletContext sc, final String attrName) {
        final Object attr = sc.getAttribute(attrName);

        if(!(attr instanceof BeanFactory)) {
            throw new IllegalStateException("Context attribute is not of type BeanFactory: " + attr);
        }

        return (BeanFactory) attr;
    }
}
