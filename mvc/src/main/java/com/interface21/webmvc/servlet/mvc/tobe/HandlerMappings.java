package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.BeanFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {
    final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void initialize(final BeanFactory beanFactory) {
        handlerMappings.add(new AnnotationHandlerMapping());
        handlerMappings.forEach(handlerMapping -> handlerMapping.initialize(beanFactory));
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest req) {
        return this.handlerMappings.stream()
                .map(mapping -> mapping.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
   	}
}
