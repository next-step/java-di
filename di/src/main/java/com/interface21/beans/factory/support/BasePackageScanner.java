package com.interface21.beans.factory.support;

import com.interface21.context.annotation.ComponentScan;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class BasePackageScanner {
    private final Class<?>[] componentScanClasses;

    public BasePackageScanner(Class<?>... componentScanClasses) {
        this.componentScanClasses = componentScanClasses;
    }

    public String[] scan() {
        Reflections reflections = new Reflections(extractPackageNames(), Scanners.TypesAnnotated);
        Set<Class<?>> typesAnnotatedWithComponentScan = reflections.getTypesAnnotatedWith(ComponentScan.class);
        return typesAnnotatedWithComponentScan.stream()
                .flatMap(type -> {
                    if (isNoDeclaredBasePackages(type)) {
                        return Stream.of(type.getPackageName());
                    }
                    return Arrays.stream(extractBasePackages(type.getAnnotation(ComponentScan.class)));
                })
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

    private String[] extractBasePackages(ComponentScan componentScan) {
        if (componentScan.value().length == 0) {
            return componentScan.basePackages();
        }

        return componentScan.value();
    }
}
