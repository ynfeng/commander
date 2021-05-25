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

class CandidateTest {

    @Test
    void should_become_follower_when_receive_greater_or_equal_term_heartbeat() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        LeaderHeartbeat leaderHeartbeat = LeaderHeartbeat.builder()
            .prevLogIndex(0)
            .term(Term.create(1))
            .leaderId(MemberId.create("server1"))
            .prevLogTerm(Term.create(0))
            .leaderCommit(0)
            .build();

        candidate.handleHeartBeat(leaderHeartbeat);

        assertThat(raftContext.calledBecomeFollower(), is(true));
    }

    @Test
    void should_not_become_follower_when_receive_lower_term_heartbeat() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        LeaderHeartbeat leaderHeartbeat = LeaderHeartbeat.builder()
            .prevLogIndex(0)
            .term(Term.create(0))
            .leaderId(MemberId.create("server1"))
            .prevLogTerm(Term.create(0))
            .leaderCommit(0)
            .build();

        candidate.handleHeartBeat(leaderHeartbeat);

        assertThat(raftContext.calledBecomeFollower(), is(false));
    }

    @Test
    void should_decline_vote_when_receive_lower_term_vote_request() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(0))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();

        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(false));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(response.term(), is(Term.create(1)));
    }

    @Test
    void should_decline_vote_when_already_vote_to_other_in_same_term() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();

        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(false));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(response.term(), is(Term.create(1)));
    }

    @Test
    void should_vote_when_receive_already_voted_member_vote_request() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server2"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();

        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(true));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(response.term(), is(Term.create(1)));
    }

    @Test
    void should_reset_election_timer_when_receive_same_term_heartbeat() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        LeaderHeartbeat leaderHeartbeat = LeaderHeartbeat.builder()
            .prevLogIndex(0)
            .term(Term.create(1))
            .leaderId(MemberId.create("server1"))
            .prevLogTerm(Term.create(0))
            .leaderCommit(0)
            .build();

        candidate.handleHeartBeat(leaderHeartbeat);

        assertThat(raftContext.calledResetElectionTimer(), is(true));
    }

    @Test
    void should_not_reset_election_timer_when_receive_greater_term_heartbeat() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        LeaderHeartbeat leaderHeartbeat = LeaderHeartbeat.builder()
            .prevLogIndex(0)
            .term(Term.create(2))
            .leaderId(MemberId.create("server1"))
            .prevLogTerm(Term.create(0))
            .leaderCommit(0)
            .build();

        candidate.handleHeartBeat(leaderHeartbeat);

        assertThat(raftContext.calledResetElectionTimer(), is(false));
    }

    @Test
    void should_not_reset_election_timer_when_receive_lower_term_heartbeat() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        LeaderHeartbeat leaderHeartbeat = LeaderHeartbeat.builder()
            .prevLogIndex(0)
            .term(Term.create(0))
            .leaderId(MemberId.create("server1"))
            .prevLogTerm(Term.create(0))
            .leaderCommit(0)
            .build();

        candidate.handleHeartBeat(leaderHeartbeat);

        assertThat(raftContext.calledResetElectionTimer(), is(false));
    }

    @Test
    void should_vote_when_receive_term_greater_or_equal_current_term_and_log_as_fresh_as_local() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(2))
            .candidateId(MemberId.create("server3"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();

        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(true));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(response.term(), is(Term.create(1)));
    }

    @Test
    void should_decline_vote_when_receive_vote_request_term_less_than_current_term() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(0))
            .candidateId(MemberId.create("server3"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();

        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(false));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(response.term(), is(Term.create(1)));
    }

    @Test
    void should_decline_vote_when_receive_vote_request_last_log_index_less_than_current_term() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        raftContext.setLastLogIndex(10);
        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(2))
            .candidateId(MemberId.create("server3"))
            .lastLogIndex(9)
            .lastLogTerm(Term.create(0))
            .build();

        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(false));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(response.term(), is(Term.create(1)));
    }

    @Test
    void should_decline_vote_when_receive_vote_request_last_log_term_less_than_current_term() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        raftContext.setLastLogIndex(10);
        raftContext.setLastLogTerm(Term.create(10));
        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(2))
            .candidateId(MemberId.create("server3"))
            .lastLogIndex(10)
            .lastLogTerm(Term.create(9))
            .build();

        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(false));
        assertThat(response.voterId(), is(MemberId.create("server2")));
        assertThat(response.term(), is(Term.create(1)));
    }
}
