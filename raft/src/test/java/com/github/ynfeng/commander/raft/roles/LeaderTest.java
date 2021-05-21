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

class LeaderTest {

    @Test
    void should_become_candidate_when_receive_greater_term_vote_request() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Leader leader = new Leader(raftContext, 1000);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(2))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = leader.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(true));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(raftContext.calledBecomeCandidate(), is(true));
        assertThat(raftContext.calledUpdateTerm(), is(Term.create(2)));
    }

    @Test
    void should_deceline_vote_when_receive_lower_term_vote_request() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Leader leader = new Leader(raftContext, 1000);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(0))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = leader.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(false));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(raftContext.calledBecomeCandidate(), is(false));
    }

    @Test
    void should_become_follower_when_receive_greater_term_heartbeat() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Leader leader = new Leader(raftContext, 1000);

        LeaderHeartbeat heartbeat = LeaderHeartbeat.builder()
            .term(Term.create(2))
            .build();
        leader.handleHeartBeat(heartbeat);

        assertThat(raftContext.calledBecomeFollower(), is(true));
    }

    @Test
    void should_become_candicate_when_receive_same_term_heartbeat() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Leader leader = new Leader(raftContext, 1000);

        LeaderHeartbeat heartbeat = LeaderHeartbeat.builder()
            .term(Term.create(1))
            .build();
        leader.handleHeartBeat(heartbeat);

        assertThat(raftContext.calledBecomeCandidate(), is(true));
    }
}
