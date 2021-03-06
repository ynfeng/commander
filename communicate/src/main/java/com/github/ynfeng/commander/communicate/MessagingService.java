package com.github.ynfeng.commander.communicate;

import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Manageable;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface MessagingService extends Manageable {
    default CompletableFuture<Void> sendAsync(Address address, Message message) {
        return sendAsync(address, message, true);
    }

    CompletableFuture<Void> sendAsync(Address address, Message message, boolean keepAlive);

    default CompletableFuture<byte[]> sendAndReceive(Address address, Message message) {
        return sendAndReceive(address, message, null, true);
    }

    default CompletableFuture<byte[]> sendAndReceive(Address address, Message message, boolean keepAlive) {
        return sendAndReceive(address, message, null, keepAlive);
    }

    default CompletableFuture<byte[]> sendAndReceive(Address address, Message message, Duration timeout) {
        return sendAndReceive(address, message, timeout, true);
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    CompletableFuture<byte[]> sendAndReceive(Address address, Message message, Duration timeout, boolean keepAlive);

    void registerHandler(String type, BiConsumer<Address, byte[]> handler);

    void registerHandler(String type, BiFunction<Address, byte[], byte[]> handler);

    void unregisterHandler(String type);

    class Message {
        private final String subject;
        private final byte[] payload;

        public Message(String subject, byte[] payload) {
            this.subject = subject;
            this.payload = payload.clone();
        }

        public String subject() {
            return subject;
        }

        public byte[] payload() {
            return payload.clone();
        }
    }
}
