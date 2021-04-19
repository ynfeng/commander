package com.github.ynfeng.commander.raft;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class RaftMemberTest {

    @Test
    void should_create_raft_member() {
        RaftMember raftMember = new RaftMember(MemberId.of("member1"));

        assertThat(raftMember.isFollower(), is(true));
    }
}
