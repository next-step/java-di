package com.interface21.beans.factory.support;

import com.interface21.context.annotation.ComponentScan;
import java.util.Arrays;
import java.util.stream.Stream;

public class BasePackageScanner {
    private final Class<?>[] componentScanClasses;

    public BasePackageScanner(Class<?>... componentScanClasses) {
        this.componentScanClasses = componentScanClasses;
    }

    public String[] scan() {
        return Arrays.stream(componentScanClasses)
                .flatMap(this::getPackageNames)
                .toArray(String[]::new);
    }

    private Stream<String> getPackageNames(Class<?> type) {
        if (isNoDeclaredBasePackages(type)) {
            return Stream.of(type.getPackageName());
        }
        return Arrays.stream(extractBasePackages(type.getAnnotation(ComponentScan.class)));
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
