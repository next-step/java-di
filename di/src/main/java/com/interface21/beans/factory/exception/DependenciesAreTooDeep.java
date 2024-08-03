package com.interface21.beans.factory.exception;

import java.util.Collection;
import java.util.StringJoiner;

public class DependenciesAreTooDeep extends RuntimeException {
    public DependenciesAreTooDeep(Collection<Class<?>> classesNotInitiated) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (Class<?> aClass : classesNotInitiated) {
            stringJoiner.add(aClass.getName());
        }

        throw new RuntimeException("빈 생성 중에 의존 관계가 너무 깊어 생성에 실패했습니다: " + stringJoiner);

    }
}
