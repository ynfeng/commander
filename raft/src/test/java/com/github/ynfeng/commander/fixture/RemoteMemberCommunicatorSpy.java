package com.github.ynfeng.commander.fixture;

import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class SpyRemoteMemberCommunicator implements RemoteMemberCommunicator {

    @Override
    public <T extends Response> CompletableFuture<T> send(RemoteMember remoteMember, Request request) {
        return null;
    }

    @Override
    public <T extends Request> void registerHandler(Class<T> requestType, Function<T, ? extends Response> action) {

    }
}
