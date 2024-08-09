package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.method.support.HandlerMethodArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandlerExecutionParser {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecutionParser.class);

    private static final List<HandlerMethodArgumentResolver> argumentResolvers = List.of(
        new HttpRequestArgumentResolver(),
        new HttpResponseArgumentResolver(),
        new RequestParamArgumentResolver(),
        new PathVariableArgumentResolver(),
        new ModelArgumentResolver()
    );

    public static Map<HandlerKey, HandlerExecution> parse(Map<Class<?>, Object> controllers) {
        final var handlers = new HashMap<HandlerKey, HandlerExecution>();
        for (var controller : controllers.values()) {
            addHandlerExecution(handlers, controller, controller.getClass().getMethods());
        }
        return handlers;
    }

    private static void addHandlerExecution(final Map<HandlerKey, HandlerExecution> handlerExecutions,
                                            final Object target,
                                            final Method[] methods) {
        Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .forEach(method -> {
                final var requestMapping = method.getAnnotation(RequestMapping.class);
                handlerExecutions.putAll(createHandlerExecutions(target, method, requestMapping));
                log.debug("register handlerExecution : url is {}, request method : {}, method is {}",
                    requestMapping.value(), requestMapping.method(), method);
            });
    }

    private static Map<HandlerKey, HandlerExecution> createHandlerExecutions(final Object target, final Method method,
                                                                             final RequestMapping requestMapping) {
        return mapHandlerKeys(requestMapping.value(), requestMapping.method())
            .stream()
            .collect(Collectors.toMap(
                handlerKey -> handlerKey,
                handlerKey -> new HandlerExecution(argumentResolvers, target, method)
            ));
    }

    private static List<HandlerKey> mapHandlerKeys(final String value, final RequestMethod[] originalMethods) {
        var targetMethods = originalMethods;
        if (targetMethods.length == 0) {
            targetMethods = RequestMethod.values();
        }
        return Arrays.stream(targetMethods)
            .map(method -> new HandlerKey(value, method))
            .collect(Collectors.toList());
    }
}
