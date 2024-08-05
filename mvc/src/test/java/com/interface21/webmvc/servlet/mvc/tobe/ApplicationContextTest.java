package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.Test;
import samples.TestConfiguration;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationContextTest {

    @Test
    void 스캔된_HandlerExecution을_반환한다() {
        ApplicationContext applicationContext = new ApplicationContext(List.of(TestConfiguration.class));
        Map<HandlerKey, HandlerExecution> actual = applicationContext.scan();
        assertThat(actual).containsOnlyKeys(
                new HandlerKey("/get-test", RequestMethod.GET),
                new HandlerKey("/post-test", RequestMethod.POST)
        );
    }
}
