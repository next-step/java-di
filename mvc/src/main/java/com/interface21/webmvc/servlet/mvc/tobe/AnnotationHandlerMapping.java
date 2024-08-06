package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.ApplicationContext;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.support.AnnotationConfigWebApplicationContext;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ApplicationContext applicationContext;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.handlerExecutions = new HandlerExecutions();
    }

    public void initialize() {
        applicationContext.getBeanClasses()
                .stream()
                .filter(beanClass -> beanClass.isAnnotationPresent(Controller.class))
                .map(applicationContext::getBean)
                .collect(Collectors.toMap(
                        Object::getClass,
                        Function.identity()))
                .forEach((clazz, bean) -> handlerExecutions.addHandlerExecution(bean, clazz.getMethods()));
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestUri = request.getRequestURI();
        final var requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        log.debug("requestUri : {}, requestMethod : {}", requestUri, requestMethod);
        return handlerExecutions.getHandler(new HandlerKey(requestUri, requestMethod));
    }
}
