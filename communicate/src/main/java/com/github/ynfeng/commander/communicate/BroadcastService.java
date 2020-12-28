package com.github.ynfeng.commander.communicate;

import com.github.ynfeng.commander.support.Manageable;
import java.util.function.Consumer;

public interface BroadcastService extends Manageable {
    void broadcast(String subject, byte[] payload);

    void addListener(String subject, Consumer<byte[]> listener);

    void removeListener(String subject, Consumer<byte[]> listener);

    int numOfSubjectOfListeners(String subject);
}
