package com.github.ynfeng.commander.cluster.communicate;

import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Manageable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public interface MessagingService extends Manageable {
    CompletableFuture<Void> sendAsync(Address address, Message message, boolean keepAlive);

    CompletableFuture<byte[]> sendAndReceive(Address address, Message message, boolean keepAlive);

    void registerHandler(String type, BiFunction<Address, byte[], CompletableFuture<byte[]>> handler);

    void unregisterHandler(String type);
}
