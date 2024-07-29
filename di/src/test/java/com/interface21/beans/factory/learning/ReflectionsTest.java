package com.interface21.beans.factory.learning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.context.stereotype.Component;
import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.Repository;
import com.interface21.context.stereotype.Service;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ReflectionsTest {

    private static final String 테스트_패키지_PREFIX = "com.interface21.beans.factory.learning";
    private static final String 애노테이션이_정의된_패키지_PREFIX = "com.interface21.context.stereotype";

    @Controller
    private static class TestController {}
    @Service
    private static class TestService {}
    @Repository
    private static class TestRepository {}
    @Component
    private static class TestComponent {}

    // getTypesAnnotatedWith(Component.class)가 해당 애노테이션이 기입된 클래스를 스캔하지 않는다.
    @DisplayName("@Controller, @Service, @Repository가 선언된 패키지를 스캔하지 않으면 @Component가 달린 클래스만 스캔한다.")
    @Test
    void reflectionsOnlyThisPackage() {
        Reflections reflections = new Reflections(테스트_패키지_PREFIX, Scanners.TypesAnnotated, Scanners.SubTypes, Scanners.MethodsAnnotated);

        Set<Class<?>> annotatedWith = reflections.getTypesAnnotatedWith(Component.class);
        assertAll(
                () -> assertThat(annotatedWith).hasSize(1),
                () -> assertThat(annotatedWith).containsOnly(TestComponent.class)
        );
    }

    // getTypesAnnotatedWith(Component.class)가 해당 애노테이션이 기입된 클래스를 함께 스캔한다.
    @DisplayName("@Controller, @Service, @Repository가 선언된 패키지를 함께 스캔하면 @Component 기준으로 클래스를 찾을 때, 해당 애노테이션이 기입된 클래스도 모두 찾는다.")
    @Test
    void reflectionsThisPackageAndStereoTypePackage() {
        Reflections reflections = new Reflections(테스트_패키지_PREFIX, 애노테이션이_정의된_패키지_PREFIX, Scanners.TypesAnnotated, Scanners.SubTypes, Scanners.MethodsAnnotated);

        Set<Class<?>> annotatedWith = reflections.getTypesAnnotatedWith(Component.class)
                .stream()
                .filter(Predicate.not(Class::isAnnotation))
                .collect(Collectors.toSet());

        assertAll(
                () -> assertThat(annotatedWith).hasSize(4),
                () -> assertThat(annotatedWith).containsOnly(TestComponent.class, TestService.class, TestController.class, TestRepository.class)
        );
    }
}
