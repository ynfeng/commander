package com.github.ynfeng.commander.raft.protocol;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;

public class RequestVoteResponse implements Response {
    private final Term term;
    private final boolean voteGranted;
    private final MemberId voterId;

    private RequestVoteResponse(Term term, MemberId voterId, boolean voteGranted) {
        this.term = term;
        this.voteGranted = voteGranted;
        this.voterId = voterId;
    }

    public static RequestVoteResponse voted(Term currentTerm, MemberId voterId) {
        return new RequestVoteResponse(currentTerm, voterId, true);
    }

    public static RequestVoteResponse declined(Term currentTerm, MemberId voterId) {
        return new RequestVoteResponse(currentTerm, voterId, false);
    }

    public boolean isVoteGranted() {
        return voteGranted;
    }

    public Term term() {
        return term;
    }

    public MemberId voterId() {
        return voterId;
    }
}
