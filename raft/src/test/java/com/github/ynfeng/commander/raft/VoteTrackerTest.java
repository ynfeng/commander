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

    @Test
    void should_record_vote() {
        VoteTracker voteTracker = new VoteTracker();

        voteTracker.voteToMe(MemberId.create("server1"));

        assertThat(voteTracker.isVoteToMe(MemberId.create("server1")), is(true));
        assertThat(voteTracker.isVoteToMe(MemberId.create("server2")), is(false));
    }

    @Test
    void can_vote_when_did_not_vote_to_any_member() {
        VoteTracker voteTracker = new VoteTracker();

        assertThat(voteTracker.canVote(Term.create(0), MemberId.create("server1")), is(true));
    }

    @Test
    void can_not_vote_when_alreay_vote_to_greater_term() {
        VoteTracker voteTracker = new VoteTracker();

        voteTracker.recordVoteCast(Term.create(1), MemberId.create("server1"));

        assertThat(voteTracker.canVote(Term.create(0), MemberId.create("server1")), is(false));
    }

    @Test
    void can_vote_when_already_vote_to_lower_term() {
        VoteTracker voteTracker = new VoteTracker();

        voteTracker.recordVoteCast(Term.create(1), MemberId.create("server1"));

        assertThat(voteTracker.canVote(Term.create(2), MemberId.create("server1")), is(true));
    }

    @Test
    void can_vote_when_alreay_vote_same_member_in_same_term() {
        VoteTracker voteTracker = new VoteTracker();

        voteTracker.recordVoteCast(Term.create(1), MemberId.create("server1"));

        assertThat(voteTracker.canVote(Term.create(1), MemberId.create("server1")), is(true));
    }
}
