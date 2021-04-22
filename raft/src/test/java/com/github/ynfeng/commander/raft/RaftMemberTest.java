package com.github.ynfeng.commander.raft;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class RaftMemberTest {

    @Test
    void should_create_raft_member() {
        RaftMember raftMember = new RaftMember(MemberId.of("member1"));

        assertThat(raftMember.isFollower(), is(true));
        assertThat(raftMember.id(), is(MemberId.of("member1")));
    }

    @Test
    void should_become_candidate_when_election_timeout() {
        RaftMember raftMember = new RaftMember(MemberId.of("member1"));
        raftMember.start();

        await().atMost(1, TimeUnit.SECONDS)
            .until(() ->raftMember.isCandicate(), is(true));

        raftMember.shutdown();
    }
}
