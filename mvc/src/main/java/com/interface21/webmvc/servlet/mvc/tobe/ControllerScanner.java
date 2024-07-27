package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.SingletonBeanDefinition;
import com.interface21.context.stereotype.Component;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.method.support.HandlerMethodArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.*;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private static final List<HandlerMethodArgumentResolver> argumentResolvers = List.of(
        new HttpRequestArgumentResolver(),
        new HttpResponseArgumentResolver(),
        new RequestParamArgumentResolver(),
        new PathVariableArgumentResolver(),
        new ModelArgumentResolver()
    );

    public Map<Class<?>, BeanDefinition> scanBean(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated, Scanners.SubTypes);
        return Stream.of(Component.class, Controller.class, Service.class, Repository.class)
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .collect(toMap(clazz -> clazz, SingletonBeanDefinition::new));
    }

    public Map<HandlerKey, HandlerExecution> scan(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated, Scanners.SubTypes, Scanners.MethodsAnnotated);
        final var handlers = new HashMap<HandlerKey, HandlerExecution>();
        final var controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            Object target = ReflectionUtils.newInstance(controller);
            addHandlerExecution(handlers, target, controller.getMethods());
        }
        return handlers;
    }

    private void addHandlerExecution(final Map<HandlerKey, HandlerExecution> handlerExecutions,
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
