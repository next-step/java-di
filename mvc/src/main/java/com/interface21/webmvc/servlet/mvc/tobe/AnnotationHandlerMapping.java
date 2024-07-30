package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.support.BeanScanner;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final BeanScanner beanScanner = new BeanScanner(basePackage);
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory(beanScanner.scan());
        beanFactory.initialize();
        handlerExecutions.putAll(initHandlerExecutions(beanFactory));
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Map<HandlerKey, HandlerExecution> initHandlerExecutions(final DefaultListableBeanFactory beanFactory) {
        final Map<Class<?>, Object> controllers = beanFactory.getBeanClasses()
                .stream()
                .filter(beanClass -> beanClass.isAnnotationPresent(Controller.class))
                .map(beanFactory::getBean)
                .collect(Collectors.toMap(
                        Object::getClass,
                        Function.identity()
                ));
        final ControllerScanner controllerScanner = new ControllerScanner();
        return controllerScanner.init(controllers);
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
