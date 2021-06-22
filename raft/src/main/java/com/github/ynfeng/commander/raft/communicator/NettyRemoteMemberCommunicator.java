package com.github.ynfeng.commander.raft.communicator;

import com.github.ynfeng.commander.communicate.MessagingService;
import com.github.ynfeng.commander.communicate.impl.NettyMessagingService;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.protocol.ProtocolSerializer;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.ManageableSupport;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class NettyRemoteMemberCommunicator extends ManageableSupport implements RemoteMemberCommunicator {
    private final MessagingService messagingService;
    private final ProtocolSerializer serializer = new ProtocolSerializer();

    public NettyRemoteMemberCommunicator(String clusterId, Address localAddress) {
        messagingService = new NettyMessagingService(clusterId, localAddress);
    }

    @Override
    public <T extends Response> CompletableFuture<T> send(RemoteMember remoteMember, Request request) {
        MessagingService.Message message = new MessagingService.Message(
            request.getClass().getSimpleName(), serializer.encode(request));
        return messagingService
            .sendAndReceive(remoteMember.address(), message, true)
            .thenApplyAsync(serializer::decode);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Request> void registerHandler(Class<T> requestType, Function<T, ? extends Response> action) {
        messagingService.registerHandler(requestType.getSimpleName(),
            (address, bytes) -> {
                Request request = serializer.decode(bytes);
                Response response = action.apply((T) request);
                return serializer.encode(response);
            });
    }

    @Override
    protected void doStart() {
        messagingService.start();
    }

    @Override
    protected void doShutdown() {
        messagingService.shutdown();
    }
}
