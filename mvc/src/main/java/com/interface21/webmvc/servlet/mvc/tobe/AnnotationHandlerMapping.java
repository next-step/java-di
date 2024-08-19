package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.ApplicationContext;
import com.interface21.context.support.tobe.HandlerExecutionsMap;
import com.interface21.context.support.tobe.HandlerMappingBuilder;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ApplicationContext applicationContext;
    private HandlerExecutionsMap handlerExecutions;

    public AnnotationHandlerMapping(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize() {
        registerAllHandlerMappings();

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerAllHandlerMappings() {
        handlerExecutions = new HandlerMappingBuilder(applicationContext).build();
    }

    @Nullable
    public Object getHandler(final HttpServletRequest request) {
        final var requestUri = request.getRequestURI();
        final var requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        log.debug("requestUri : {}, requestMethod : {}", requestUri, requestMethod);

        HandlerKey requestHandlerKey = new HandlerKey(requestUri, requestMethod);

        return handlerExecutions.getMatchedHandlerExecution(requestHandlerKey);
    }
}
