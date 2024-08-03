package com.interface21.beans.factory.support;

import com.interface21.context.annotation.ComponentScan;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class BasePackageScanner {
    public String[] scan() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage(""));

        // basePackage 필드에 값이 없는 클래스는 해당 메서드가 선언된 패키지 스캔
        List<String> basePackages1 = reflections.getTypesAnnotatedWith(ComponentScan.class)
                .stream()
                .filter(Predicate.not(Class::isAnnotation))
                .filter(clazz -> {
                    ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
                    return componentScan.value().length == 0 && componentScan.basePackages().length == 0;
                })
                .map(Class::getPackageName)
                .toList();

        // basePackages 필드에 값을 가진 클래스 처리
        List<String> basePackages2 = reflections.getTypesAnnotatedWith(ComponentScan.class)
                .stream()
                .filter(Predicate.not(Class::isAnnotation))
                .map(clazz -> clazz.getAnnotation(ComponentScan.class))
                .filter(componentScan -> componentScan.value().length != 0 || componentScan.basePackages().length != 0)
                .flatMap(componentScan -> {
                    if (componentScan.value().length == 0) {
                        return Arrays.stream(componentScan.basePackages());
                    }

                    return Arrays.stream(componentScan.value());
                })
                .toList();

        return Stream.concat(basePackages1.stream(), basePackages2.stream())
                .toArray(String[]::new);
    }
}
