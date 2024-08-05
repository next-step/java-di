package com.interface21.beans.factory.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.MultiPackageScanConfig;
import samples.SampleScanConfig;

class BasePackageScannerTest {
    @DisplayName("@ComponentScan 에 basePackage가 지정되지 않으면 애노테이션이 선언된 클래스의 패키지를 basePackage로 한다.")
    @Test
    void scan1() {
        BasePackageScanner basePackageScanner = new BasePackageScanner(SampleScanConfig.class);

        String[] basePackages = basePackageScanner.scan();

        assertThat(basePackages).containsOnly("samples");
    }

    @DisplayName("@ComponentScan에 basePacakge가 지정된 경우 지정된 패키지를 basePackage로 한다.")
    @Test
    void scan2() {
        BasePackageScanner basePackageScanner = new BasePackageScanner(MultiPackageScanConfig.class);

        String[] basePackages = basePackageScanner.scan();

        assertThat(basePackages).containsOnly("samples.scantest", "samples.scantest2");
    }
}