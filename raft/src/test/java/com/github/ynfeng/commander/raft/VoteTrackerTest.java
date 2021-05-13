package com.github.ynfeng.commander.raft;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class VoteTrackerTest {

    @Test
    void should_record_vote_cast() {
        VoteTracker voteTracker = new VoteTracker();
        voteTracker.recordVoteCast(Term.create(0), MemberId.create("server1"));

        assertThat(voteTracker.isAlreadyVoteTo(Term.create(0), MemberId.create("server1")), is(true));
        assertThat(voteTracker.isAlreadyVoteTo(Term.create(0), MemberId.create("server2")), is(false));
        assertThat(voteTracker.isAlreadyVoteTo(Term.create(1), MemberId.create("server1")), is(false));
    }
}
