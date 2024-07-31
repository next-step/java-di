package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.method.support.HandlerMethodArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.HttpRequestArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.HttpResponseArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.ModelArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.PathVariableArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.RequestParamArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandlerExecutions {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecutions.class);

    private static final List<HandlerMethodArgumentResolver> argumentResolvers = List.of(
            new HttpRequestArgumentResolver(),
            new HttpResponseArgumentResolver(),
            new RequestParamArgumentResolver(),
            new PathVariableArgumentResolver(),
            new ModelArgumentResolver()
    );

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public HandlerExecutions() {
        this.handlerExecutions = new HashMap<>();
    }

    public void addHandlerExecution(final Object target,
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

    public HandlerExecution getHandler(final HandlerKey handlerKey) {
        return handlerExecutions.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isMatch(handlerKey))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }
}
