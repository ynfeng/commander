package com.github.ynfeng.commander.engine;

import java.util.HashMap;

public class Variables extends HashMap<String, Object> {
    public static final Variables EMPTY = new Variables();

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
