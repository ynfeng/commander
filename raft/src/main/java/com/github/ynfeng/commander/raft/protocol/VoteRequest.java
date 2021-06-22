package com.github.ynfeng.commander.raft.protocol;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;

public class VoteRequest implements Request {
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

    public MemberId candidateId() {
        return candidateId;
    }

    public long lastLogIndex() {
        return lastLogIndex;
    }

    public Term lastLogTerm() {
        return lastLogTerm;
    }

    public static class Builder {
        private Term term;
        private MemberId candidateId;
        private Long lastLogIndex;
        private Term lastLogTerm;

        private Builder() {
        }

        public Builder term(Term term) {
            this.term = term;
            return this;
        }

        public Builder candidateId(MemberId candidateId) {
            this.candidateId = candidateId;
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

        public VoteRequest build() {
            VoteRequest voteRequest = new VoteRequest();
            voteRequest.term = term;
            voteRequest.candidateId = candidateId;
            voteRequest.lastLogIndex = lastLogIndex;
            voteRequest.lastLogTerm = lastLogTerm;
            return voteRequest;
        }
    }
}
