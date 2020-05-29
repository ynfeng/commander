package com.github.ynfeng.commander.core;

import java.util.concurrent.ConcurrentHashMap;

public class Parameters extends ConcurrentHashMap<String, Object> {
    public static Parameters copy(Parameters input) {
        Parameters parameters = new Parameters();
        input.entrySet().forEach(entry -> {
            parameters.put(entry.getKey(), entry.getValue());
        });
        return parameters;
    }
}
