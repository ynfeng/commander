package com.github.ynfeng.commander.engine.command;

import akka.actor.typed.ActorRef;

public class GetNodeExecutingVariable implements EngineCommand {
    private ActorRef<GetNodeExecutingVariableResponse> replyTo;

    public GetNodeExecutingVariable(ActorRef<GetNodeExecutingVariableResponse> replyTo) {
        this.replyTo = replyTo;
    }

    public ActorRef<GetNodeExecutingVariableResponse> replyTo() {
        return replyTo;
    }
}
