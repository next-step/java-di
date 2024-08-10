package com.interface21.beans.factory.exception;

public class NoSuchBeanDefinitionException extends RuntimeException {

    public static final String format = "%s 타입의 빈을 찾을 수 없습니다.";

    public NoSuchBeanDefinitionException(Class<?> requiredType) {
        super(String.format(format, requiredType));
    }
}
