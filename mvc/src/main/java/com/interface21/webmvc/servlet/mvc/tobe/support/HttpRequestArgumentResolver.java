package com.interface21.webmvc.servlet.mvc.tobe.support;

import com.interface21.core.MethodParameter;
import com.interface21.web.method.support.ArgumentResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpRequestArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getType() == HttpServletRequest.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return request;
    }
}
