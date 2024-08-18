package com.interface21.webmvc.servlet.mvc.tobe;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final List<HandlerMethodArgumentResolver> argumentResolvers = List.of(
        new HttpRequestArgumentResolver(),
        new HttpResponseArgumentResolver(),
        new RequestParamArgumentResolver(),
        new PathVariableArgumentResolver(),
        new ModelArgumentResolver()
    );

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final Map<Class<?>, Object> controllers;

    public AnnotationHandlerMapping(final Map<Class<?>, Object> controllers) {
        this.handlerExecutions = new HashMap<>();
        this.controllers = controllers;
    }

    public void initialize() {
        handlerExecutions.putAll(this.addHandlerExecutions(controllers));
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Map<HandlerKey, HandlerExecution> addHandlerExecutions(Map<Class<?>, Object> controllers) {
        final var handlers = new HashMap<HandlerKey, HandlerExecution>();
        for (Map.Entry<Class<?>, Object> map : controllers.entrySet()) {
            Class<?> clazz = map.getKey();
            addHandlerExecution(handlers, map.getValue(), clazz.getMethods());
        }
        return handlers;
    }

    private void addHandlerExecution(
        final Map<HandlerKey, HandlerExecution> handlerExecutions,
        final Object target,
        final Method[] methods
    ) {
        Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .forEach(method -> {
                final var requestMapping = method.getAnnotation(RequestMapping.class);
                handlerExecutions.putAll(createHandlerExecutions(target, method, requestMapping));
                log.info("register handlerExecution : url is {}, request method : {}, method is {}",
                    requestMapping.value(), requestMapping.method(), method);
            });
    }

    private Map<HandlerKey, HandlerExecution> createHandlerExecutions(final Object target, final Method method, final RequestMapping requestMapping) {
        return mapHandlerKeys(requestMapping.value(), requestMapping.method())
            .stream()
            .collect(Collectors.toMap(
                handlerKey -> handlerKey,
                handlerKey -> new HandlerExecution(argumentResolvers, target, method)
            ));
    }

    private List<HandlerKey> mapHandlerKeys(final String value, final RequestMethod[] originalMethods) {
        var targetMethods = originalMethods;
        if (targetMethods.length == 0) {
            targetMethods = RequestMethod.values();
        }
        return Arrays.stream(targetMethods)
            .map(method -> new HandlerKey(value, method))
            .collect(Collectors.toList());
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
