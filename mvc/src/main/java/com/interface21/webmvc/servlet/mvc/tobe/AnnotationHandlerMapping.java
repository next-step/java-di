package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final MappingRegistry mappingRegistry = new MappingRegistry();

    @Override
    public void initialize(final BeanFactory beanFactory) {
        log.info("Initialized AnnotationHandlerMapping!");

        final List<Object> matchingBeans = BeanFactoryUtils.beansOfAnnotated(beanFactory, Controller.class);

        matchingBeans.forEach(handler -> mappingRegistry.registerControllers(matchingBeans));

        mappingRegistry.addArgumentResolver(new ServletHandlerMethodArgumentResolver());
        mappingRegistry.addArgumentResolver(new RequestParamMethodArgumentResolver(false));
        mappingRegistry.addArgumentResolver(new PathVariableMethodArgumentResolver());
        mappingRegistry.addArgumentResolver(new ServletModelAttributeMethodProcessor());
        mappingRegistry.addArgumentResolver(new RequestParamMethodArgumentResolver(true));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return this.mappingRegistry.getMethod(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
    }
}
