package com.github.ynfeng.commander.communicate.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.communicate.UnicastService;
import com.github.ynfeng.commander.support.Address;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NettyUnicastServiceTest {
    private UnicastService unicastService;

    @BeforeEach
    public void setup() {
        unicastService = new NettyUnicastService(1985);
        unicastService.start();
        assertThat(unicastService.isStarted(), is(true));
    }

    @AfterEach
    public void destory() {
        unicastService.shutdown();
        assertThat(unicastService.isStarted(), is(false));
    }

    @Test
    public void should_add_and_remove_listener() {
        BiConsumer<Address, byte[]> listener = (addr, bytes) -> {
        };
        assertThat(unicastService.numOfSubjectOfListeners("test"), is(0));

        unicastService.addListener("test", listener);
        assertThat(unicastService.numOfSubjectOfListeners("test"), is(1));

        unicastService.removeListener("test", listener);
        assertThat(unicastService.numOfSubjectOfListeners("test"), is(0));
    }

    @Test
    public void should_unicast_and_receive() throws InterruptedException {
        Object waitObj = new Object();
        AtomicReference<Address> recvAddress = new AtomicReference<>();
        AtomicReference<byte[]> recvBytes = new AtomicReference<>();
        unicastService.addListener("test", (address, bytes) -> {
            recvAddress.set(address);
            recvBytes.set(bytes);
            synchronized (waitObj) {
                waitObj.notify();
            }
        });
        unicastService.unicast(Address.of("127.0.0.1", 1985), "test", "test".getBytes());

        synchronized (waitObj) {
            waitObj.wait(1000);
        }

        assertThat(recvAddress.get().host(), is("127.0.0.1"));
        assertThat(recvBytes.get(), is("test".getBytes()));
    }

    @Test
    public void should_not_receive_not_subscribed_subject() throws InterruptedException {
        Object waitObj = new Object();
        AtomicReference<Address> recvAddress = new AtomicReference<>();
        AtomicReference<byte[]> recvBytes = new AtomicReference<>();
        unicastService.addListener("test", (address, bytes) -> {
            recvAddress.set(address);
            recvBytes.set(bytes);
            synchronized (waitObj) {
                waitObj.notify();
            }
        });
        unicastService.unicast(Address.of("127.0.0.1", 1985), "unsucribed", "test".getBytes());

        synchronized (waitObj) {
            waitObj.wait(200);
        }

        assertThat(recvAddress.get(), nullValue());
    }
}
