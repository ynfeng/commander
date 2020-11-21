package com.github.ynfeng.commander.cluster.primitive;

public interface PrimitiveFactory {
    <K, V> DistributedMap<K, V> createDistributedMap(String name);

    <E> DistributedList<E> createDistributedList(String name);
}
