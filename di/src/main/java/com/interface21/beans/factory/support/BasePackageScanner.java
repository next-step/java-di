package com.interface21.beans.factory.support;

import com.interface21.context.annotation.ComponentScan;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class BasePackageScanner {
    private static final String ALL_PACKAGE = "";

    private final Class<?>[] componentScanClasses;

    public BasePackageScanner(Class<?>... componentScanClasses) {
        this.componentScanClasses = componentScanClasses;
    }

    public String[] scan() {
        Reflections reflections = new Reflections(extractPackageNames(), Scanners.TypesAnnotated);
        Set<Class<?>> typesAnnotatedWithComponentScan = reflections.getTypesAnnotatedWith(ComponentScan.class);

        List<String> classPackages = typesAnnotatedWithComponentScan.stream()
                .filter(this::isNoDeclaredBasePackages)
                .map(Class::getPackageName)
                .toList();

        List<String> basePackages = typesAnnotatedWithComponentScan.stream()
                .filter(this::isDeclaredBasePackages)
                .flatMap(this::extractBasePackages)
                .toList();

        return Stream.concat(classPackages.stream(), basePackages.stream())
                .toArray(String[]::new);
    }

    private String[] extractPackageNames() {
        return Arrays.stream(componentScanClasses)
                .map(Class::getPackageName)
                .toArray(String[]::new);
    }

    private boolean isNoDeclaredBasePackages(Class<?> clazz) {
        ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
        return componentScan.value().length == 0 && componentScan.basePackages().length == 0;
    }

    private boolean isDeclaredBasePackages(Class<?> clazz) {
        ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
        return componentScan.value().length != 0 || componentScan.basePackages().length != 0;
    }

    private Stream<String> extractBasePackages(Class<?> clazz) {
        ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
        if (componentScan.value().length == 0) {
            return Arrays.stream(componentScan.basePackages());
        }

        return Arrays.stream(componentScan.value());
    }
}
