package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.support.BeanScanner;
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

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var beanScanner = new BeanScanner(Arrays.toString(basePackage));
        BeanFactory beanFactory = new DefaultListableBeanFactory(beanScanner.scan());
        Map<Class<?>, Object> controllers = beanFactory.getControllers();
        handlerExecutions.putAll(HandlerExecutionParser.parse(controllers));
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
