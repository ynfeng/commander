package com.github.ynfeng.commander.engine;

import akka.actor.typed.ActorSystem;
import com.github.ynfeng.commander.definition.ProcessDefinition;

public class ActorProcessEngine implements ProcessEngine {
    private ActorSystem<EngineCommand> engineActor;

    @Override
    public void startup() {
        engineActor = ActorSystem.create(EngineActor.create(), "process-engine");
    }

    @Override
    public void startProcess(ProcessDefinition processDefinition) {
        engineActor.tell(new StartProcess(processDefinition));
    }
}
