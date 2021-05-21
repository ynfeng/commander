package com.github.ynfeng.commander.raft.roles;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.fixture.RaftContextMock;
import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;
import org.junit.jupiter.api.Test;

class CandidateTest {

    @Test
    void should_decline_to_vote_when_request_term_less_than_current_term() {
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
        assertThat(response.term(), is(Term.create(1)));
        assertThat(response.voterId(), is(MemberId.create("server2")));
    }

    @Test
    void should_vote_when_request_term_greater_than_current_term() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(0));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);

        assertThat(response.isVoted(), is(true));
        assertThat(response.term(), is(Term.create(1)));
        assertThat(response.voterId(), is(MemberId.create("server2")));
    }

    @Test
    void should_decline_when_already_voted_for_other_candidate() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(0));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);
        assertThat(response.isVoted(), is(true));
        assertThat(response.term(), is(Term.create(1)));
        assertThat(response.voterId(), is(MemberId.create("server2")));

        voteRequest = VoteRequest.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server3"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        response = candidate.handleRequestVote(voteRequest);
        assertThat(response.isVoted(), is(false));
        assertThat(response.term(), is(Term.create(1)));
        assertThat(response.voterId(), is(MemberId.create("server2")));
    }

    @Test
    void should_vote_to_new_term() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(0));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);
        assertThat(response.isVoted(), is(true));

        voteRequest = VoteRequest.builder()
            .term(Term.create(2))
            .candidateId(MemberId.create("server3"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        response = candidate.handleRequestVote(voteRequest);
        assertThat(response.isVoted(), is(true));
    }

    @Test
    void should_update_to_newest_term() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(0));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);
        assertThat(response.isVoted(), is(true));

        assertThat(raftContext.currentTerm(), is(Term.create(1)));
    }

    @Test
    void should_decline_vote_when_last_log_was_not_newest() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(0));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        raftContext.setLastLogIndex(1);
        raftContext.setLastLogTerm(Term.create(1));

        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);
        assertThat(response.isVoted(), is(false));
    }

    @Test
    void should_vote_when_last_log_was_newest() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(0));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        raftContext.setLastLogIndex(1);
        raftContext.setLastLogTerm(Term.create(1));

        Candidate candidate = new Candidate(raftContext);

        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(3)
            .lastLogTerm(Term.create(3))
            .build();
        RequestVoteResponse response = candidate.handleRequestVote(voteRequest);
        assertThat(response.isVoted(), is(true));
    }
}
