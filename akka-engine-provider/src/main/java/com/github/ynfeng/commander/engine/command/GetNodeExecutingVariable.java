package com.github.ynfeng.commander.engine.command;

import akka.actor.typed.ActorRef;

public class GetNodeExecutingVariable implements EngineCommand {
    private ActorRef<NodeExecutingVariableResponse> replyTo;

    public GetNodeExecutingVariable(ActorRef<NodeExecutingVariableResponse> replyTo) {
        this.replyTo = replyTo;
    }

    public ActorRef<NodeExecutingVariableResponse> replyTo() {
        return replyTo;
    }
}
