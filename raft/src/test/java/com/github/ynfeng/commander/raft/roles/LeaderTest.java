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
    void should_become_candicate_when_receive_greater_term_vote_request() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Leader leader = new Leader(raftContext, 100);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(2))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();

        RequestVoteResponse response = leader.handleRequestVote(voteRequest);
        assertThat(response.isVoted(), is(false));
        assertThat(raftContext.currentTerm(), is(Term.create(2)));
        assertThat(raftContext.calledBecomeCandidate(), is(true));
    }

    @Test
    void should_not_become_candicate_when_receive_less_or_equal_term_vote_request() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Leader leader = new Leader(raftContext, 100);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();

        RequestVoteResponse response = leader.handleRequestVote(voteRequest);
        assertThat(response.isVoted(), is(false));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(response.term(), is(Term.create(1)));
        assertThat(raftContext.currentTerm(), is(Term.create(1)));
        assertThat(raftContext.calledBecomeCandidate(), is(false));
    }

    @Test
    void should_become_follower_when_receive_greater_term_heartbeat() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Leader leader = new Leader(raftContext, 100);

        LeaderHeartbeat leaderHeartbeat = LeaderHeartbeat.builder()
            .prevLogIndex(0)
            .term(Term.create(2))
            .leaderId(MemberId.create("server1"))
            .prevLogTerm(Term.create(0))
            .leaderCommit(0)
            .build();

        leader.handleHeartBeat(leaderHeartbeat);

        assertThat(raftContext.calledBecomeFollower(), is(true));
    }
}
