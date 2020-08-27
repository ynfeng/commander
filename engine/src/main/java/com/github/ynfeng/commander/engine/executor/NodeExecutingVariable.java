package com.github.ynfeng.commander.engine.executor;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;

public class NodeExecutingVariable {
    private final Map<String, Object> vars = Maps.newHashMap();

    public <T> T get(String key, T t) {
        Optional<T> optional = (Optional<T>) Optional.ofNullable(vars.get(key));
        return optional.orElse(t);
    }

    protected void put(String key, Object value) {
        vars.put(key, value);
    }
}
