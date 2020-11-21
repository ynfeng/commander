package com.github.ynfeng.commander.cluster.primitive;

public interface PrimitiveFactory {
    <K, V> ConsistenDistributedMap<K, V> createConsistenMap(String name);
}
