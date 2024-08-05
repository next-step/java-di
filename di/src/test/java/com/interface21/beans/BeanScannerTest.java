package com.interface21.beans;

import com.interface21.context.stereotype.Component;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannerTest {

    private final BeanScanner beanScanner = new BeanScanner("samples");

    @DisplayName("패키지 내의 클래스를 스캔 후 반환 한다")
    @Test
    public void scanClassesTypeAnnotatedWith() throws Exception {
        final Set<Class<?>> actual = beanScanner.scanClassesTypeAnnotatedWith();

        assertThat(actual).hasSize(6);
    }
}
