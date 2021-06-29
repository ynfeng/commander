package com.github.ynfeng.commander.raft;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.RepeatedTest;

class ElectionTimerTest {

    @RepeatedTest(3)
    void should_timeout() {
        AtomicBoolean timeout = new AtomicBoolean(false);

        ElectionTimer timer = new ElectionTimer(100, () -> {
            timeout.set(true);
        });
        timer.start();

        await()
            .atMost(Duration.ofMillis(700))
            .until(timeout::get, is(true));
    }

}
