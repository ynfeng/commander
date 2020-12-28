package com.github.ynfeng.commander.communicate.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.communicate.MessagingService;
import com.github.ynfeng.commander.support.Address;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class NettyMessagingServiceTest {

    @Test
    public void should_start_and_shutdown() {
        NettyMessagingService messagingService = new NettyMessagingService("test", Address.of("127.0.0.1", 7892));
        messagingService.start();
        assertThat(messagingService.isStarted(), is(true));

        messagingService.shutdown();
        assertThat(messagingService.isStarted(), is(false));
    }

    @Test
    public void should_send_and_listen_message() throws Exception {
        NettyMessagingService peer1 = new NettyMessagingService("test", Address.of("127.0.0.1", 7892));
        NettyMessagingService peer2 = new NettyMessagingService("test", Address.of("127.0.0.1", 1990));
        try {
            peer1.start();
            peer2.start();
            CompletableFuture<byte[]> result = new CompletableFuture<>();
            peer2.registerHandler("hello", (address, bytes) -> {
                result.complete(bytes);
            });

            MessagingService.Message greeting = new MessagingService.Message("hello", "Hello there!".getBytes());
            peer1.sendAsync(Address.of("127.0.0.1", 1990), greeting).join();

            byte[] receiveBytes = result.get(1, TimeUnit.SECONDS);
            assertThat(receiveBytes, is("Hello there!".getBytes()));

            peer2.unregisterHandler("hello");
        } finally {
            peer1.shutdown();
            peer2.shutdown();
        }
    }

    @Test
    public void should_send_and_receive() throws Exception {
        Address peer1Addr = Address.of("127.0.0.1", 7892);
        Address peer2Addr = Address.of("127.0.0.1", 1990);
        NettyMessagingService peer1 = new NettyMessagingService("test", peer1Addr);
        NettyMessagingService peer2 = new NettyMessagingService("test", peer2Addr);
        try {
            peer1.start();
            peer2.start();
            peer2.registerHandler("hello", (addr, payload) -> {
                return "How are you.".getBytes();
            });
            MessagingService.Message greeting = new MessagingService.Message("hello", "Hello there!".getBytes());
            byte[] reply = peer1.sendAndReceive(peer2Addr, greeting).get(1, TimeUnit.SECONDS);
            assertThat(reply, is("How are you.".getBytes()));
        } finally {
            peer2.shutdown();
            peer1.shutdown();
        }

    }
}
