package com.interface21.context.support;

import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanScanner {

    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    private final String[] basePackages;
    private final Reflections reflections;
    private final List<Class<? extends Annotation>> beanAnnotations;

    public BeanScanner(String... basePackages) {
        this.basePackages = basePackages;
        this.reflections = new Reflections(basePackages);
        this.beanAnnotations = List.of(Controller.class, Service.class, Repository.class);
    }

    public Set<Class<?>> scan() {
        log.info("BeanScanner scan basePackages: {}", basePackages);
        return beanAnnotations.stream()
                              .map(reflections::getTypesAnnotatedWith)
                              .flatMap(Set::stream)
                              .collect(Collectors.toSet());
    }
}
