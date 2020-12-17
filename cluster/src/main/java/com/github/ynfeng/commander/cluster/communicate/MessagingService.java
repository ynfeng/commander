package com.github.ynfeng.commander.cluster.communicate;

import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Manageable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public interface MessagingService extends Manageable {
    default CompletableFuture<Void> sendAsync(Address address, Message message) {
        return sendAsync(address, message, true);
    }

    CompletableFuture<Void> sendAsync(Address address, Message message, boolean keepAlive);

    default CompletableFuture<byte[]> sendAndReceive(Address address, Message message) {
        return sendAndReceive(address, message, true);
    }

    CompletableFuture<byte[]> sendAndReceive(Address address, Message message, boolean keepAlive);

    void registerHandler(String type, BiFunction<Address, byte[], CompletableFuture<byte[]>> handler);

    void unregisterHandler(String type);

    class Message {
        private final String type;
        private final byte[] payload;

        public Message(String type, byte[] payload) {
            this.type = type;
            this.payload = payload.clone();
        }

        public String getType() {
            return type;
        }

        public byte[] getPayload() {
            return payload.clone();
        }
    }
}
