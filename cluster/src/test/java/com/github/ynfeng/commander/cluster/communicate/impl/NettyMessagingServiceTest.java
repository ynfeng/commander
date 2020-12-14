package com.github.ynfeng.commander.cluster.communicate.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.support.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NettyMessagingServiceTest {
    private NettyMessagingService messagingService;

    @BeforeEach
    public void setup() {
        messagingService = new NettyMessagingService(Address.of("127.0.0.1", 7892));
        messagingService.start();
    }

    @AfterEach
    public void destory() {
        messagingService.shutdown();
    }

    @Test
    public void should_start_and_shutdown() {
        messagingService.start();
        assertThat(messagingService.isStarted(), is(true));

        messagingService.shutdown();
        assertThat(messagingService.isStarted(), is(false));
    }

}
