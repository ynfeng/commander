package com.github.ynfeng.commander.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.function.Supplier;

public class KryoSerializer implements Serializer {
    private static final int DEFAULT_POOL_SIZE = 100;
    private final KryoPool kryoPool;
    private final InputPool inputPool;
    private final OutputPool outputPool;

    public KryoSerializer(SerializationTypes types) {
        kryoPool = new KryoPool(types, DEFAULT_POOL_SIZE);
        inputPool = new InputPool(DEFAULT_POOL_SIZE);
        outputPool = new OutputPool(DEFAULT_POOL_SIZE);
    }


    @Override
    public <T> byte[] encode(T object) {
        Kryo kryo = kryoPool.obtain();
        Output output = outputPool.obtain();
        return runAndRecyle(() -> {
            kryo.writeClassAndObject(output, object);
            return output.toBytes();
        }, () -> kryoPool.free(kryo), () -> outputPool.free(output));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T decode(byte[] bytes) {
        Kryo kryo = kryoPool.obtain();
        Input input = inputPool.obtain();
        return runAndRecyle(() -> {
            input.setInputStream(new ByteArrayInputStream(bytes));
            return (T) kryo.readClassAndObject(input);
        }, () -> kryoPool.free(kryo), () -> inputPool.free(input));
    }

    public <T> T runAndRecyle(Supplier<T> provider, Runnable... cleaners) {
        try {
            return provider.get();
        } finally {
            Arrays.stream(cleaners).forEach(Runnable::run);
        }
    }
}
