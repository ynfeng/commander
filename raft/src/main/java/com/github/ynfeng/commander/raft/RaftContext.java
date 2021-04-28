package com.github.ynfeng.commander.raft;

import java.util.List;

public interface RaftContext {
    MemberId localMermberId();

    List<RemoteMember> remoteMembers();

    RemoteMemberCommunicator remoteMemberCommunicator();

    Term lastLogTerm();

    long lastLogIndex();

    Term currentTerm();

    void tryUpdateCurrentTerm(Term term);

    int quorum();

    void becomeLeader();

    long lastCommitIndex();

    long prevLogIndex();

    Term prevLogTerm();

    void resetElectionTimer();
}