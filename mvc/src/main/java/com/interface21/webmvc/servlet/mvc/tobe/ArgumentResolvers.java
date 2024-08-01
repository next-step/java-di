package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.core.MethodParameter;
import com.interface21.web.method.support.HandlerMethodArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.HttpRequestArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.HttpResponseArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.ModelArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.PathVariableArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.support.RequestParamArgumentResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class ArgumentResolvers {

    private final List<HandlerMethodArgumentResolver> argumentResolvers;

    public ArgumentResolvers() {
        argumentResolvers = List.of(
                new HttpRequestArgumentResolver(),
                new HttpResponseArgumentResolver(),
                new RequestParamArgumentResolver(),
                new PathVariableArgumentResolver(),
                new ModelArgumentResolver()
        );
    }

    public Object getArguments(final MethodParameter methodParameter, final HttpServletRequest request, final HttpServletResponse response) {
        return argumentResolvers.stream()
                .filter(resolver -> resolver.supportsParameter(methodParameter))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No suitable resolver for argument: " + methodParameter.getType()))
                .resolveArgument(methodParameter, request, response);
    }
}
