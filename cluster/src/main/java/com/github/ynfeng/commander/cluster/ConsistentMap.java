package com.github.ynfeng.commander.cluster;

public interface ConsistentMap<K, V> {
    void put(K k, V v);

    V get(K k);

    void update(K k, V v);

    void remove(K k);

    void destory();
}
