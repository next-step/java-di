package com.interface21.context.annotation;

public interface AnnotationConfigRegistry {

    /**
     * Perform a scan within the specified base packages.
     *
     * @param configuration the configuration to scan for component classes
     */
    void scan(Class<?> configuration);
}
