package com.github.ynfeng.commander.raft;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;

import com.github.ynfeng.commander.support.Address;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class RaftServerTest {

    @Test
    public void should_start_raft_server() {
        RaftServer raftServer = RaftServer.builder()
            .localAddress(Address.of("127.0.0.1", 8080))
            .localMemberId(MemberId.create("server1"))
            .raftConfig(RaftConfig.builder()
                .electionTimeoutDetectionInterval(500)
                .build())
            .build();

        raftServer.start();

        await()
            .atMost(1, TimeUnit.SECONDS)
            .until(raftServer::isStarted, is(true));
    }
}
