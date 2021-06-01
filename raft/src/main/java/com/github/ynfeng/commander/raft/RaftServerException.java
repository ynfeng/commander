package com.github.ynfeng.commander.raft;

public class RaftServerException extends RuntimeException {
    public RaftServerException(Throwable t) {
        super(t);
    }
}
