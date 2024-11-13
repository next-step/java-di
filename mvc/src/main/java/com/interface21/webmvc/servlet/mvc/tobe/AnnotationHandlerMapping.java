package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.support.BeanScanner;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.support.AnnotationConfigWebApplicationContext;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.method.support.HandlerMethodArgumentResolver;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.support.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final AnnotationConfigWebApplicationContext applicationContext;

    public AnnotationHandlerMapping(
            AnnotationConfigWebApplicationContext applicationContext
    ) {
        this.handlerExecutions = new HashMap<>();
        this.applicationContext = applicationContext;
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Set<Class<?>> classSet = applicationContext.getBeanClasses().stream()
                .filter(c -> c.isAnnotationPresent(Controller.class))
                .collect(Collectors.toSet());

        for (Class<?> clazz : classSet) {
            addHandlerExecution(
                    applicationContext.getBean(clazz),
                    clazz.getMethods()
            );
        }
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

    private final List<HandlerMethodArgumentResolver> argumentResolvers = List.of(
            new HttpRequestArgumentResolver(),
            new HttpResponseArgumentResolver(),
            new RequestParamArgumentResolver(),
            new PathVariableArgumentResolver(),
            new ModelArgumentResolver()
    );

    private void addHandlerExecution(
            final Object target,
            final Method[] methods
    ) {
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
}
