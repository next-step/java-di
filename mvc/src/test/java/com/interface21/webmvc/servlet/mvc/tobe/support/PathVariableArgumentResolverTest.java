package com.interface21.webmvc.servlet.mvc.tobe.support;

import com.interface21.core.MethodParameter;
import com.interface21.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PathVariableArgumentResolverTest {

    private PathVariableArgumentResolver argumentResolver = new PathVariableArgumentResolver();

    @DisplayName("PathVariable Argument Resolver Test")
    @ParameterizedTest(name = "MethodParameter: {0}")
    @MethodSource({"samplePathVariableMethodParameter"})
    void PathVariableResolveArguments(HttpServletRequest request, MethodParameter methodParameter, Object expectedResult) {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object result = argumentResolver.resolveArgument(methodParameter, request, response);

        assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> samplePathVariableMethodParameter() throws NoSuchMethodException {
        final Method mockStringPathVariableMethod = MockArgumentResolverController.class.getDeclaredMethod("mockPathVariableMethod", String.class);
        final PathVariable stringAnnotation = (PathVariable) mockStringPathVariableMethod.getParameterAnnotations()[0][0];

        final Method mockIntPathVariableMethod = MockArgumentResolverController.class.getDeclaredMethod("mockPathVariableMethod", int.class);
        final PathVariable intAnnotation = (PathVariable) mockIntPathVariableMethod.getParameterAnnotations()[0][0];
        String paramName = "id";
        return Stream.of(
            Arguments.of(new MockHttpServletRequest("GET", "/pathVariable/jun"),
                new MethodParameter(mockStringPathVariableMethod, String.class, new Annotation[]{stringAnnotation}, paramName),
                "jun"),

            Arguments.of(new MockHttpServletRequest("GET", "/pathVariable/user/50"),
                new MethodParameter(mockIntPathVariableMethod, Integer.class, new Annotation[]{intAnnotation}, paramName),
                    50)
        );
    }
}
