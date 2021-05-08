package com.github.ynfeng.commander.raft;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.fixture.RaftContextMock;
import com.github.ynfeng.commander.raft.protocol.RequestVote;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.roles.Candidate;
import org.junit.jupiter.api.Test;

class RaftRoleVoteTest {

    @Test
    void should_decline_to_vote_when_request_term_less_than_current_term() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(1));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        RequestVote requestVote = RequestVote.builder()
            .term(Term.create(0))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = candidate.handleRequestVote(requestVote);

        assertThat(response.isVoteGranted(), is(false));
        assertThat(response.term(), is(Term.create(1)));
        assertThat(response.voterId(), is(MemberId.create("server2")));
    }

    @Test
    void should_vote_when_request_term_greater_or_equal_current_term() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(0));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        RequestVote requestVote = RequestVote.builder()
            .term(Term.create(0))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = candidate.handleRequestVote(requestVote);

        assertThat(response.isVoteGranted(), is(true));
        assertThat(response.term(), is(Term.create(0)));
        assertThat(response.voterId(), is(MemberId.create("server2")));
    }

    @Test
    void should_decline_when_already_voted_for_other_candidate() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(0));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        RequestVote requestVote = RequestVote.builder()
            .term(Term.create(0))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = candidate.handleRequestVote(requestVote);
        assertThat(response.isVoteGranted(), is(true));
        assertThat(response.term(), is(Term.create(0)));
        assertThat(response.voterId(), is(MemberId.create("server2")));

        requestVote = RequestVote.builder()
            .term(Term.create(0))
            .candidateId(MemberId.create("server3"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        response = candidate.handleRequestVote(requestVote);
        assertThat(response.isVoteGranted(), is(false));
        assertThat(response.term(), is(Term.create(0)));
        assertThat(response.voterId(), is(MemberId.create("server2")));
    }

    @Test
    void should_vote_when_already_voted_to_current_candidate() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(0));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        RequestVote requestVote = RequestVote.builder()
            .term(Term.create(0))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = candidate.handleRequestVote(requestVote);
        assertThat(response.isVoteGranted(), is(true));
        assertThat(response.term(), is(Term.create(0)));
        assertThat(response.voterId(), is(MemberId.create("server2")));

        requestVote = RequestVote.builder()
            .term(Term.create(0))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        response = candidate.handleRequestVote(requestVote);
        assertThat(response.isVoteGranted(), is(true));
        assertThat(response.term(), is(Term.create(0)));
        assertThat(response.voterId(), is(MemberId.create("server2")));
    }

    @Test
    void should_vote_to_new_term() {
        RaftContextMock raftContext = new RaftContextMock();
        raftContext.setCurrentTerm(Term.create(0));
        raftContext.setLocalMemberId(MemberId.create("server2"));
        Candidate candidate = new Candidate(raftContext);

        RequestVote requestVote = RequestVote.builder()
            .term(Term.create(0))
            .candidateId(MemberId.create("server1"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        RequestVoteResponse response = candidate.handleRequestVote(requestVote);
        assertThat(response.isVoteGranted(), is(true));

        requestVote = RequestVote.builder()
            .term(Term.create(1))
            .candidateId(MemberId.create("server3"))
            .lastLogIndex(0)
            .lastLogTerm(Term.create(0))
            .build();
        response = candidate.handleRequestVote(requestVote);
        assertThat(response.isVoteGranted(), is(true));
    }
}
