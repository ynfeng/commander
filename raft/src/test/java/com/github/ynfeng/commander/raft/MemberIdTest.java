package com.github.ynfeng.commander.raft;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class MemberIdTest {

    @Test
    void should_create_member_id() {
        MemberId memberId = MemberId.of("member1");

        assertThat(memberId.id(), is("member1"));
    }
}
