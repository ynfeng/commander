package com.github.ynfeng.commander.engine.command;

import akka.actor.typed.ActorRef;

public class SetNodeExecutingVariable implements EngineCommand {
    private final String key;
    private final Object val;
    private final ActorRef<SetNodeExecutingVariableResponse> replyTo;

    public SetNodeExecutingVariable(String key, Object val,
                                    ActorRef<SetNodeExecutingVariableResponse> replyTo) {
        this.key = key;
        this.val = val;
        this.replyTo = replyTo;
    }

    public String key() {
        return key;
    }

    public Object value() {
        return val;
    }

    public ActorRef<SetNodeExecutingVariableResponse> replyTo() {
        return replyTo;
    }
}
