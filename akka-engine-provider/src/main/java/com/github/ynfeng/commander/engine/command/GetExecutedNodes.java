package com.github.ynfeng.commander.engine.command;

import akka.actor.typed.ActorRef;

public class GetExecutedNodes implements EngineCommand {
    private final ActorRef<GetExecutedNodesResponse> replyTo;

    public GetExecutedNodes(ActorRef<GetExecutedNodesResponse> replyTo) {
        this.replyTo = replyTo;
    }

    public ActorRef<GetExecutedNodesResponse> replyTo() {
        return replyTo;
    }
}
