package com.github.ynfeng.commander.engine;

import akka.actor.typed.ActorRef;
import com.github.ynfeng.commander.engine.command.EngineCommand;
import com.google.common.collect.Maps;
import java.util.Map;

public class ExecutorActors {
    private final Map<String, ActorRef<EngineCommand>> refs = Maps.newHashMap();

    public void add(String refName, ActorRef<EngineCommand> executorRef) {
        refs.put(refName, executorRef);
    }

    public ActorRef<EngineCommand> remove(String refName) {
        ActorRef<EngineCommand> ref = refs.get(refName);
        refs.remove(refName);
        return ref;
    }

    public ActorRef<EngineCommand> get(String refName) {
        return refs.get(refName);
    }
}
