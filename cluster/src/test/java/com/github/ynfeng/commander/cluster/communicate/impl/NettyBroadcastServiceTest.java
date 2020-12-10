package com.github.ynfeng.commander.cluster.communicate.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.support.Address;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NettyBroadcastServiceTest {
    private NettyBroadcastService broadcastService;

    @BeforeEach
    public void setup() {
        broadcastService = new NettyBroadcastService(
            Address.of("127.0.0.1", 1234), Address.of("230.0.0.1", 1234));
        broadcastService.start();
    }

    @AfterEach
    public void destory() {
        broadcastService.shutdown();
    }

    @Test
    public void should_add_and_remove_listener() {
        Consumer<byte[]> listener = bytes -> {
        };
        broadcastService.addListener("testSubject", listener);
        assertThat(broadcastService.numOfSubjectOfListeners("testSubject"), is(1));

        broadcastService.removeListener("testSubject", listener);
        assertThat(broadcastService.numOfSubjectOfListeners("testSubject"), is(0));
    }

    @Test
    public void should_broadcast_and_receive() throws InterruptedException {
        Object waitObj = new Object();
        AtomicReference<String> actual = new AtomicReference<>();
        broadcastService.addListener("test", bytes -> {
            synchronized (waitObj) {
                actual.set(new String(bytes));
                waitObj.notify();
            }
        });

        broadcastService.broadcast("test", "test".getBytes());
        synchronized (waitObj) {
            waitObj.wait(1000);
        }

        assertThat(actual.get(), is("test"));
    }

    @Test
    public void should_not_receive_not_subscribed_subject() throws InterruptedException {
        Object waitObj = new Object();
        AtomicReference<String> actual = new AtomicReference<>();
        broadcastService.addListener("test", bytes -> {
            synchronized (waitObj) {
                actual.set(new String(bytes));
                waitObj.notify();
            }
        });

        broadcastService.broadcast("notSubscribe", "test".getBytes());
        synchronized (waitObj) {
            waitObj.wait(100);
        }

        assertThat(actual.get(), nullValue());
    }
}
