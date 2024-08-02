package com.interface21.beans.factory.support;

public class CircularReferenceException extends IllegalArgumentException {
    public CircularReferenceException() {
        super("순환 참조인 빈이 있어 생성할 수 없습니다.");
    }
}
