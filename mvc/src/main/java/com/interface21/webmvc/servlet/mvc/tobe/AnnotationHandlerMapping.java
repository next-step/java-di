package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;
import com.interface21.context.support.WebApplicationContext;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ApplicationContext applicationContext;
    public AnnotationHandlerMapping(ApplicationContext applicationContext) {
        this.handlerExecutions = new HashMap<>();
        this.applicationContext = applicationContext;
    }

    public void initialize() {
        final var beanScanner = new BeanScanner();
        handlerExecutions.putAll(beanScanner.scan(applicationContext.getBeanFactory()));
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestUri = request.getRequestURI();
        final var requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        log.debug("requestUri : {}, requestMethod : {}", requestUri, requestMethod);
        return getHandlerInternal(new HandlerKey(requestUri, requestMethod));
    }

    private HandlerExecution getHandlerInternal(final HandlerKey requestHandlerKey) {
        for (HandlerKey handlerKey : handlerExecutions.keySet()) {
            if (handlerKey.isMatch(requestHandlerKey)) {
                return handlerExecutions.get(handlerKey);
            }
        }
        return null;
    }
}
