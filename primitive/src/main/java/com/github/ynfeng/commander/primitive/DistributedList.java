package com.github.ynfeng.commander.primitive;

import java.util.List;

public interface DistributedList<E> extends Destroyable {
    void add(E e);

    int size();

    List<E> toList();
}
