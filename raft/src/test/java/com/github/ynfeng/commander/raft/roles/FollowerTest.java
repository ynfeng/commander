package com.github.ynfeng.commander.raft.roles;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.fixture.RaftContextMock;
import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;
import org.junit.jupiter.api.Test;

class FollowerTest {

    @Test
    void should_vote_when_receive_greater_term_vote_request() {
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

        assertThat(response.isVoted(), is(true));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(response.term(), is(Term.create(2)));
    }

    @Test
    void should_declined_when_redive_same_term_vote_request() {
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
    }

    @Test
    void should_become_candidate_after_vote() {
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

        assertThat(response.isVoted(), is(true));
        assertThat(raftContext.calledBecomeCandidate(), is(true));
    }

    @Test
    void should_upadte_term_after_vote() {
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

        assertThat(response.isVoted(), is(true));
        assertThat(raftContext.currentTerm(), is(Term.create(2)));
    }

    @Test
    void should_reset_election_timer_when_recieve_same_term_heartbeat() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setCurrentLeader(MemberId.create("server1"));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Follower follower = new Follower(raftContext);

        LeaderHeartbeat heartbeat = LeaderHeartbeat
            .builder()
            .term(Term.create(1))
            .leaderId(MemberId.create("server1"))
            .prevLogTerm(Term.create(0))
            .prevLogIndex(0)
            .build();

        follower.handleHeartBeat(heartbeat);

        assertThat(raftContext.calledResetElectionTimer(), is(true));
    }
}
