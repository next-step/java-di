package com.interface21.web.method.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.core.MethodParameter;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(MethodParameter parameter);

    Object resolveArgument(
            MethodParameter parameter, HttpServletRequest request, HttpServletResponse response);
}
