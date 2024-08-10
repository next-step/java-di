package com.interface21.context.support.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.method.support.HandlerMethodArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
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

public class HandlerMappingFactory {

    private static final Logger log = LoggerFactory.getLogger(HandlerMappingFactory.class);

    private static final List<HandlerMethodArgumentResolver> argumentResolvers = List.of(
            new HttpRequestArgumentResolver(),
            new HttpResponseArgumentResolver(),
            new RequestParamArgumentResolver(),
            new PathVariableArgumentResolver(),
            new ModelArgumentResolver()
    );

    public Map<HandlerKey, HandlerExecution> getHandlerMappings(Map<Class<?>, Object> beanClasses) {
        final var handlerExecutionsMap = new HashMap<HandlerKey, HandlerExecution>();
        beanClasses.forEach((controllerClass, controllerBean) ->
                addHandlerExecutions(handlerExecutionsMap, controllerClass, controllerBean)
        );
        return handlerExecutionsMap;
    }

    private void addHandlerExecutions(HashMap<HandlerKey, HandlerExecution> handlerExecutionsMap,
                                      Class<?> clazz,
                                      Object bean) {
        Method[] methods = clazz.getMethods();
        Arrays.stream(methods)
              .filter(requestMappingMethod -> requestMappingMethod.isAnnotationPresent(RequestMapping.class))
              .forEach(requestMappingMethod -> addHandlerExecution(handlerExecutionsMap, bean, requestMappingMethod));
    }

    private void addHandlerExecution(Map<HandlerKey, HandlerExecution> handlerExecutionsMap,
                                     Object target,
                                     Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        handlerExecutionsMap.putAll(
                createHandlerExecutions(target, method, requestMapping)
        );
        log.debug("register handlerExecution : url is {}, request method : {}, method is {}",
                requestMapping.value(), requestMapping.method(), method);
    }

    private Map<HandlerKey, HandlerExecution> createHandlerExecutions(final Object target,
                                                                      final Method method,
                                                                      final RequestMapping requestMapping) {
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
