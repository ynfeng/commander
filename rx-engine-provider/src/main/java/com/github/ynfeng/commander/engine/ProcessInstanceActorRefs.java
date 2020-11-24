package com.github.ynfeng.commander.engine;

import akka.actor.typed.ActorRef;
import com.github.ynfeng.commander.engine.command.EngineCommand;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;

public class ProcessInstanceActorRefs {
    private final Map<ProcessId, ActorRef<EngineCommand>> refs = Maps.newHashMap();

    public void add(ProcessId processId, ActorRef<EngineCommand> processInstanceRef) {
        refs.put(processId, processInstanceRef);
    }

    public Optional<ActorRef<EngineCommand>> get(ProcessId processId) {
        return Optional.ofNullable(refs.get(processId));
    }
}
