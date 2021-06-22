package com.github.ynfeng.commander.raft.protocol;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;

public class RequestVoteResponse implements Response {
    private Term term;
    private boolean voted;
    private MemberId voterId;

    private RequestVoteResponse() {

    }

    private RequestVoteResponse(Term term, MemberId voterId, boolean voted) {
        this.term = term;
        this.voted = voted;
        this.voterId = voterId;
    }

    public static RequestVoteResponse voted(Term currentTerm, MemberId voterId) {
        return new RequestVoteResponse(currentTerm, voterId, true);
    }

    public static RequestVoteResponse declined(Term currentTerm, MemberId voterId) {
        return new RequestVoteResponse(currentTerm, voterId, false);
    }

    public boolean isVoted() {
        return voted;
    }

    public Term term() {
        return term;
    }

    public MemberId voterId() {
        return voterId;
    }
}
