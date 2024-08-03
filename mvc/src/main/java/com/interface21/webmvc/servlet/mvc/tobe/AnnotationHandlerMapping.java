package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.ApplicationContext;
import com.interface21.context.support.tobe.BeanScanner;
import com.interface21.context.support.tobe.HandlerMappingFactory;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ApplicationContext applicationContext;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var beanScanner = new BeanScanner(applicationContext);
        final var handlerMappingBuilder = new HandlerMappingFactory(applicationContext);

        registerAllHandlerMappings(beanScanner, handlerMappingBuilder);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerAllHandlerMappings(BeanScanner beanScanner, HandlerMappingFactory handlerMappingFactory) {
        final var beanClasses = beanScanner.scan();
        final var handlerMappings = handlerMappingFactory.getHandlerMappings(beanClasses);
        handlerExecutions.putAll(handlerMappings);
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
