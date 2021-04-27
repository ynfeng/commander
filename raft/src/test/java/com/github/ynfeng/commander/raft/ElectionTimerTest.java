package com.github.ynfeng.commander.raft;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class ElectionTimerTest {

    @Test
    void should_timeout() {
        AtomicBoolean timeout = new AtomicBoolean(false);

        ElectionTimer timer = new ElectionTimer(100, () -> {
            timeout.set(true);
        });
        timer.start();

        await()
            .atMost(200, TimeUnit.MILLISECONDS)
            .until(timeout::get, is(true));
    }

}
