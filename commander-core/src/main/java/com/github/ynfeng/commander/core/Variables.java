package com.github.ynfeng.commander.core;

import java.util.concurrent.ConcurrentHashMap;

public class Variables extends ConcurrentHashMap<String, Object> {
    public static Variables copy(Variables input) {
        Variables variables = new Variables();
        input.entrySet().forEach(entry -> {
            variables.put(entry.getKey(), entry.getValue());
        });
        return variables;
    }

    public void merge(Variables params) {
        params.entrySet().forEach(entry -> {
            put(entry.getKey(), entry.getValue());
        });
    }
}
