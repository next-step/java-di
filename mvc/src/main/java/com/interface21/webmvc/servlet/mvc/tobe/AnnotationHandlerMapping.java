package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.ApplicationContext;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.method.support.HandlerMethodArgumentResolver;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.support.HttpRequestArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.HttpResponseArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.ModelArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.PathVariableArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.RequestParamArgumentResolver;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final List<HandlerMethodArgumentResolver> argumentResolvers = List.of(
        new HttpRequestArgumentResolver(),
        new HttpResponseArgumentResolver(),
        new RequestParamArgumentResolver(),
        new PathVariableArgumentResolver(),
        new ModelArgumentResolver()
    );

    private final ApplicationContext context;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;


    public AnnotationHandlerMapping(ApplicationContext context) {
        this.handlerExecutions = new HashMap<>();
        this.context = context;
    }

    @Override
    public void initialize() {
        context.getBeansWithAnnotation(Controller.class)
            .forEach(this::processController);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.entrySet()
            .stream()
            .filter(entry -> entry.getKey().isMatch(handlerKey))
            .findAny()
            .map(Map.Entry::getValue)
            .orElse(null);
    }

    private void processController(Class<?> controller, Object instance) {
        Arrays.stream(controller.getMethods())
            .filter(this::hasRequestMappingMethod)
            .flatMap(method -> createHandlerExecutions(instance, method))
            .forEach(this::putHandlerExecution);
    }

    private boolean hasRequestMappingMethod(Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    private Stream<Map.Entry<HandlerKey, HandlerExecution>> createHandlerExecutions(Object instance, Method method) {
        HandlerExecution handlerExecution = makeHandlerExecution(instance, method);
        return makeHandlerKeys(handlerExecution).entrySet().stream();
    }

    private void putHandlerExecution(Map.Entry<HandlerKey, HandlerExecution> entry) {
        handlerExecutions.put(entry.getKey(), entry.getValue());
    }

    private HandlerExecution makeHandlerExecution(Object instance, Method method) {
        return new HandlerExecution(argumentResolvers, instance, method);
    }

    private Map<HandlerKey, HandlerExecution> makeHandlerKeys(final HandlerExecution handlerExecution) {
        RequestMapping requestMapping = handlerExecution.getMethod().getAnnotation(RequestMapping.class);
        return Stream.of(requestMapping.method())
            .collect(Collectors.toMap(
                requestMethod -> new HandlerKey(requestMapping.value(), requestMethod),
                requestMethod -> handlerExecution));
    }
}
