package com.interface21.context.annotation;

public interface AnnotationConfigRegistry {

    /**
     * Perform a scan within the specified base packages.
     *
     * @param basePackages the packages to scan for component classes
     */
    void scan(String... basePackages);
}
