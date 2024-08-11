package com.interface21.beans.factory.exception;

public class NoUniqueBeanDefinitionException extends RuntimeException {

    public static final String format = "%s 타입 빈이 여러개(%d 개) 발견되어 하나를 선택할 수 없습니다.";

    public NoUniqueBeanDefinitionException(Class<?> requiredType, int size) {
        super(String.format(format, requiredType, size));
    }
}
