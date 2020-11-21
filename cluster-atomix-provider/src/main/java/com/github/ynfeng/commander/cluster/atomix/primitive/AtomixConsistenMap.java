package com.github.ynfeng.commander.cluster.atomix.primitive;

import com.github.ynfeng.commander.cluster.primitive.DistributedMap;
import io.atomix.core.Atomix;
import io.atomix.core.map.AtomicMap;
import io.atomix.core.map.AtomicMapBuilder;
import io.atomix.protocols.raft.MultiRaftProtocol;
import io.atomix.protocols.raft.ReadConsistency;
import io.atomix.utils.time.Versioned;
import java.util.Objects;

public class AtomixConsistenMap<K, V> implements DistributedMap<K, V> {
    private final AtomicMap<K, V> map;

    public AtomixConsistenMap(Atomix atomix, String name) {
        MultiRaftProtocol protocol = MultiRaftProtocol.builder()
            .withReadConsistency(ReadConsistency.LINEARIZABLE)
            .build();
        AtomicMapBuilder<K, V> mapBuilder = atomix.atomicMapBuilder(name);
        map = mapBuilder.withProtocol(protocol).build();
    }

    @Override
    public void put(K k, V v) {
        map.put(k, v);
    }

    @Override
    public V get(K k) {
        Versioned<V> value = map.get(k);
        if (Objects.nonNull(value)) {
            return value.value();
        }
        return null;
    }

    @Override
    public void update(K k, V v) {
        map.compute(k, (mk, mv) -> {
            return v;
        });
    }

    @Override
    public void remove(K k) {
        map.remove(k);
    }

    @Override
    public void destory() {
        map.clear();
        map.close();
    }
}
