package com.github.ynfeng.commander.communicate.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Host;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NettyBroadcastServiceTest {
    private NettyBroadcastService broadcastService;

    @BeforeEach
    void setup() {
        broadcastService = new NettyBroadcastService(
            Host.of("127.0.0.1"), Address.of("230.0.0.1", 1234));
        broadcastService.start();
        assertThat(broadcastService.isStarted(), is(true));
    }

    @AfterEach
    void destory() {
        broadcastService.shutdown();
        assertThat(broadcastService.isStarted(), is(false));
    }

    @Test
    void should_add_and_remove_listener() {
        Consumer<byte[]> listener = bytes -> {
        };
        broadcastService.addListener("testSubject", listener);
        assertThat(broadcastService.numOfSubjectOfListeners("testSubject"), is(1));

        broadcastService.removeListener("testSubject", listener);
        assertThat(broadcastService.numOfSubjectOfListeners("testSubject"), is(0));
    }

    @Test
    void should_broadcast_and_receive() throws InterruptedException {
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
    void should_not_receive_not_subscribed_subject() throws InterruptedException {
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
