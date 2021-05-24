package com.github.ynfeng.commander.fixture;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;

public class LeaderHeartbeatRecord {
    public final Term term;
    public final MemberId leaderId;

    public LeaderHeartbeatRecord(Term term, MemberId leaderId) {
        this.term = term;
        this.leaderId = leaderId;
    }

    public boolean isSame(Term term, MemberId leaderId) {
        return this.term.equals(term) && this.leaderId.equals(leaderId);
    }
}
