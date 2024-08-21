package com.interface21.beans.factory;

import java.util.Set;

public class CircularException extends RuntimeException {

    private final Set<Class<?>> circularBeans;

    /**
     * Create a new CircularException.
     *
     * @param circularBeans the circular Beans
     */
    public CircularException(Set<Class<?>> circularBeans) {
        super("[" + circularBeans.toString() + "]" + "CircularException 이 생겼습니다.");
        this.circularBeans = circularBeans;
    }
}
