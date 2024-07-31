package com.interface21.webmvc.servlet.mvc.tobe;


import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerExecutionsTest {

    private HandlerExecutions handlerExecutions;

    @BeforeEach
    void setUp() {
        handlerExecutions = new HandlerExecutions();
        final TestController controller = new TestController();
        handlerExecutions.addHandlerExecution(controller, TestController.class.getMethods());
    }

    @Test
    @DisplayName("HandlerKey 에 맞는 HandlerExecution 이 있다면 반환한다.")
    void getHandlerTest() {
        final HandlerKey handlerKey = new HandlerKey("/get", RequestMethod.GET);

        final HandlerExecution handlerExecution = handlerExecutions.getHandler(handlerKey);

        assertThat(handlerExecution).isNotNull();
    }

    @Test
    @DisplayName("조건에 맞는 HandlerExecution 이 없으면 null 을 반환한다.")
    void getHandlerEmptyTest() {
        final HandlerKey handlerKey = new HandlerKey("/unknown", RequestMethod.GET);

        final HandlerExecution handlerExecution = handlerExecutions.getHandler(handlerKey);

        assertThat(handlerExecution).isNull();
    }

    @ParameterizedTest
    @EnumSource
    @DisplayName("method 선언이 안되어있으면 모든 HTTP method 를 지원한다")
    void noRequestMethodTest(final RequestMethod requestMethod) {
        final HandlerKey handlerKey = new HandlerKey("/no-method", requestMethod);

        final HandlerExecution handlerExecution = handlerExecutions.getHandler(handlerKey);

        assertThat(handlerExecution).isNotNull();
    }

    static class TestController {
        @RequestMapping(value = "/get", method = RequestMethod.GET)
        public void getMethod() {
        }

        @RequestMapping(value = "/no-method")
        public void noMethod() {
        }
    }
}
