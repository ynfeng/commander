package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.atomix.primitive.AtomixConsistenMap;
import com.github.ynfeng.commander.cluster.primitive.DistributedMap;
import com.github.ynfeng.commander.cluster.primitive.PrimitiveFactory;
import io.atomix.core.Atomix;

public class AtomixPrimitiveFactory implements PrimitiveFactory {
    private final Atomix atomix;

    public AtomixPrimitiveFactory(Atomix atomix) {
        this.atomix = atomix;
    }

    @Override
    public <K, V> DistributedMap<K, V> createDistributedMap(String name) {
        return new AtomixConsistenMap<>(atomix, name);
    }
}
