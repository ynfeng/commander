package com.github.ynfeng.commander.raft;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

    @ParameterizedTest
    @MethodSource("checkAlreadyVoteArguments")
    void can_check_already_vote() {
        VoteTracker voteTracker = new VoteTracker();

        voteTracker.recordVoteCast(Term.create(1), MemberId.create("server1"));

        assertThat(voteTracker.canVote(Term.create(0), MemberId.create("server1")), is(false));
    }

    static Stream<Arguments> checkAlreadyVoteArguments() {
        return Stream.of(
            Arguments.of(0, false),
            Arguments.of(2, true),
            Arguments.of(1, true));
    }
}
