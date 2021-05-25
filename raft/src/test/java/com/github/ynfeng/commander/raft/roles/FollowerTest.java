package com.github.ynfeng.commander.raft.roles;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.fixture.RaftContextMock;
import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;
import org.junit.jupiter.api.Test;

class FollowerTest {

    @Test
    void should_become_candicate_when_receive_greater_term_vote_request() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Follower follower = new Follower(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(2))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = follower.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(false));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(response.term(), is(Term.create(2)));
        assertThat(raftContext.calledBecomeCandidate(), is(true));
    }

    @Test
    void should_not_become_candicate_when_receive_less_or_equal_term_vote_request() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Follower follower = new Follower(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = follower.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(false));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(response.term(), is(Term.create(1)));
        assertThat(raftContext.calledBecomeCandidate(), is(false));
    }
}
