package com.github.ynfeng.commander.serializer;

import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;

public class OutputPool extends Pool<Output> {
    private static final int MAX_BUFFER_SIZE = 512 * 1024;

    public OutputPool(int maximumCapacity) {
        super(true, false, maximumCapacity);
    }

    @Override
    protected Output create() {
        return new Output(MAX_BUFFER_SIZE);
    }
}
