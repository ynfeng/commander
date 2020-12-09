package com.github.ynfeng.commander.serializer;

public interface Serializer {
    static Serializer create(SerializationTypes types) {
        return new KryoSerializer(types);
    }

    <T> byte[] encode(T object);

    <T> T decode(byte[] bytes);
}
