package com.github.ynfeng.commander.cluster.atomix.primitive;

import com.github.ynfeng.commander.primitive.DistributedList;
import io.atomix.core.Atomix;
import io.atomix.protocols.raft.MultiRaftProtocol;
import io.atomix.protocols.raft.ReadConsistency;
import java.util.ArrayList;
import java.util.List;

public class AtomixConsistenList<E> implements DistributedList<E> {
    private final io.atomix.core.list.DistributedList<E> list;

    public AtomixConsistenList(Atomix atomix, String name) {
        MultiRaftProtocol protocol = MultiRaftProtocol.builder()
            .withReadConsistency(ReadConsistency.LINEARIZABLE)
            .build();
        list = atomix.<E>listBuilder(name)
            .withProtocol(protocol)
            .build();
    }

    @Override
    public void add(E e) {
        list.add(e);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public List<E> toList() {
        return new ArrayList<E>(list);
    }

    @Override
    public void destory() {
        list.clear();
        list.close();
    }
}
