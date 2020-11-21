package com.github.ynfeng.commander.cluster.primitive;

public interface DistributedMap<K, V> extends Destroyable {
    void put(K k, V v);

    V get(K k);

    void update(K k, V v);

    void remove(K k);
}
