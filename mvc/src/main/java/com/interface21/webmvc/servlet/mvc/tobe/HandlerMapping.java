package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.BeanFactory;
import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    void initialize(final BeanFactory beanFactory);

    Object getHandler(final HttpServletRequest req);
}
