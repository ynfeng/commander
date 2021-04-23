package com.github.ynfeng.commander.raft;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.support.Address;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class RaftMemberTest {

    @Test
    void should_create_raft_member() {
        RaftMember raftMember = createRaftMember("member1");

        assertThat(raftMember.isFollower(), is(true));
        assertThat(raftMember.id(), is(MemberId.of("member1")));
    }

    @Test
    void should_become_candidate_when_election_timeout() {
        RaftMember raftMember = createRaftMember("member1");
        raftMember.start();

        await().atMost(1, TimeUnit.SECONDS)
            .until(() -> raftMember.isCandicate(), is(true));

        raftMember.shutdown();
    }

    private static RaftMember createRaftMember(String memberId) {
        return RaftMember.builder()
            .memberId(MemberId.of(memberId))
            .membership(Membership.create()
                .addMember(MemberId.of("member1"), Address.of("127.0.0.1", 8081))
                .addMember(MemberId.of("member2"), Address.of("127.0.0.1", 8082))
                .addMember(MemberId.of("member3"), Address.of("127.0.0.1", 8083)))
            .localConfig(
                LocalConfig.builder()
                    .electionTimeoutDetectionInterval(100)
                    .build())
            .build();
    }
}
