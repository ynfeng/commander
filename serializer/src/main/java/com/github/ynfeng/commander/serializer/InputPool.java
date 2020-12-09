package com.github.ynfeng.commander.serializer;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.util.Pool;

public class InputPool extends Pool<Input> {
    private static final int MAX_BUFFER_SIZE = 512 * 1024;

    public InputPool(int maximumCapacity) {
        super(true, false, maximumCapacity);
    }

    @Override
    protected Input create() {
        return new Input(MAX_BUFFER_SIZE);
    }
}
