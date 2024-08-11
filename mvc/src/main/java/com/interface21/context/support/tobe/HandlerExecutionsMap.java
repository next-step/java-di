package com.interface21.context.support.tobe;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class HandlerExecutionsMap {

    private final Map<HandlerKey, HandlerExecution> map;

    public HandlerExecutionsMap() {
        this.map = new HashMap<>();
    }

    public void register(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        map.put(handlerKey, handlerExecution);
    }

    @Nullable
    public HandlerExecution getMatchedHandlerExecution(final HandlerKey requestHandlerKey) {
        for (HandlerKey handlerKey : map.keySet()) {
            if (handlerKey.isMatch(requestHandlerKey)) {
                return map.get(handlerKey);
            }
        }
        return null;
    }
}
