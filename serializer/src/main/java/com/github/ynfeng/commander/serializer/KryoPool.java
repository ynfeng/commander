package com.github.ynfeng.commander.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.Pool;

public class KryoPool extends Pool<Kryo> {
    private final SerializationTypes types;

    public KryoPool(SerializationTypes types, int maximumCapacity) {
        super(true, false, maximumCapacity);
        this.types = types;
    }

    @Override
    protected Kryo create() {
        Kryo kryo = new Kryo();
        int startId = types.startId();
        for (Class<?> clazz : types.supportTypes()) {
            kryo.register(clazz, startId++);
        }
        return kryo;
    }
}
