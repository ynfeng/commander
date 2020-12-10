package com.github.ynfeng.commander.cluster.communicate;

import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Manageable;
import java.util.function.BiConsumer;

public interface UnicastService extends Manageable {
    void unicast(Address address, String subject, byte[] payload);

    void addListener(String subject, BiConsumer<Address, byte[]> listener);

    void removeListener(String subject, BiConsumer<Address, byte[]> listener);

    int numOfSubjectOfListeners(String subject);
}
