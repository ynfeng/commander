package com.github.ynfeng.commander.engine;

import java.util.concurrent.ConcurrentHashMap;

public class Variables extends ConcurrentHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    protected static final Variables EMPTY = new Variables();

    public static Variables copy(Variables input) {
        Variables variables = new Variables();
        input.forEach(variables::put);
        return variables;
    }

    public void merge(Variables params) {
        params.forEach(this::put);
    }
}
