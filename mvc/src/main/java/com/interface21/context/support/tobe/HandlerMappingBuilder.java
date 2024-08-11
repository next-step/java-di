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
import java.util.List;
import java.util.Map;

public class HandlerMappingBuilder {

    private static final Logger log = LoggerFactory.getLogger(HandlerMappingBuilder.class);

    private static final List<HandlerMethodArgumentResolver> ARGUMENT_RESOLVERS = List.of(
            new HttpRequestArgumentResolver(),
            new HttpResponseArgumentResolver(),
            new RequestParamArgumentResolver(),
            new PathVariableArgumentResolver(),
            new ModelArgumentResolver()
    );

    private final HandlerExecutionsMap handlerExecutionsMap;
    private final Map<Class<?>, Object> beanClasses;

    public HandlerMappingBuilder(final Map<Class<?>, Object> beanClasses) {
        this.handlerExecutionsMap = new HandlerExecutionsMap();

        this.beanClasses = beanClasses;
    }

    public HandlerExecutionsMap build() {
        beanClasses.forEach(this::registerEachClass);

        return handlerExecutionsMap;
    }

    private void registerEachClass(final Class<?> clazz, final Object bean) {
        final Method[] methods = clazz.getMethods();
        Arrays.stream(methods)
              .filter(requestMappingMethod -> requestMappingMethod.isAnnotationPresent(RequestMapping.class))
              .forEach(requestMappingMethod -> registerEachMethod(bean, requestMappingMethod));
    }

    private void registerEachMethod(final Object target, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String requestUrl = requestMapping.value();
        final RequestMethod[] requestMethods = getTargetMethodsFromRequestMapping(requestMapping.method());

        Arrays.stream(requestMethods)
              .forEach(requestMethod ->
                      registerEachRequestMethod(target, method, requestMethod, requestUrl)
              );

        log.debug("register handlerExecution : requestUrl is {}, request method : {}, method is {}",
                requestUrl, requestMethods, method);
    }

    private static RequestMethod[] getTargetMethodsFromRequestMapping(RequestMethod[] targetMethods) {
        if (targetMethods.length == 0) {
            return RequestMethod.values();
        }

        return targetMethods;
    }

    private void registerEachRequestMethod(final Object target,
                                           final Method method,
                                           final RequestMethod requestMethod,
                                           final String url) {
        final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        final HandlerExecution handlerExecution = new HandlerExecution(ARGUMENT_RESOLVERS, target, method);

        handlerExecutionsMap.register(handlerKey, handlerExecution);
    }
}
