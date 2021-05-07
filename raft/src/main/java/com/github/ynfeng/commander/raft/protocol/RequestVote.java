package com.github.ynfeng.commander.raft.protocol;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;

public class RequestVote implements Request {
    private Term term;
    private MemberId candidateId;
    private long lastLogIndex;
    private Term lastLogTerm;

    public static Builder builder() {
        return new Builder();
    }

    public Term term() {
        return term;
    }

    public static class Builder {
        private Term term;
        private MemberId cadidateId;
        private Long lastLogIndex;
        private Term lastLogTerm;

        private Builder() {
        }

        public Builder term(Term term) {
            this.term = term;
            return this;
        }

        public Builder candidateId(MemberId candidateId) {
            this.cadidateId = candidateId;
            return this;
        }

        public Builder lastLogIndex(long lastLogIndex) {
            this.lastLogIndex = lastLogIndex;
            return this;
        }

        public Builder lastLogTerm(Term lastLogTerm) {
            this.lastLogTerm = lastLogTerm;
            return this;
        }

        public RequestVote build() {
            RequestVote requestVote = new RequestVote();
            requestVote.term = term;
            requestVote.candidateId = cadidateId;
            requestVote.lastLogIndex = lastLogIndex;
            requestVote.lastLogTerm = lastLogTerm;
            return requestVote;
        }
    }
}
