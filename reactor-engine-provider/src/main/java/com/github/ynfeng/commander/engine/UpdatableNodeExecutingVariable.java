package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;

public class UpdatableNodeExecutingVariable extends NodeExecutingVariable {

    @Override
    public void put(String key, Object value) {
        super.put(key, value);
    }
}
