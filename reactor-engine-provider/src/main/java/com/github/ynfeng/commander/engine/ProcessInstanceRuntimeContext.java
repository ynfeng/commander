package com.github.ynfeng.commander.engine;

import akka.actor.typed.ActorRef;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.command.EngineCommand;
import java.util.List;
import java.util.Optional;

public class ProcessInstanceRuntimeContext {
    private final ExecutorActors executorActors = new ExecutorActors();
    private final ReadyNodes readyNodes = new ReadyNodes();
    private final RunningNodes runningNodes = new RunningNodes();
    private final ExecutedNodes executedNodes = new ExecutedNodes();

    public void addExecutorActors(String refName, ActorRef<EngineCommand> executorRef) {
        executorActors.add(refName, executorRef);
    }

    public ActorRef<EngineCommand> getExecutorActor(String refName) {
        return executorActors.get(refName);
    }

    public ActorRef<EngineCommand> removeExecutorActor(String refName) {
        return executorActors.remove(refName);
    }

    public void addReadyNode(NodeDefinition nextNode) {
        readyNodes.add(nextNode);
    }

    public NodeDefinition nextReadyNode() {
        return readyNodes.poll();
    }

    public Optional<NodeDefinition> getRunningNode(String nodeRefName) {
        return runningNodes.get(nodeRefName);
    }

    public void removeRunningNode(String refName) {
        runningNodes.remove(refName);
    }

    public void addRunningNode(NodeDefinition nextNode) {
        runningNodes.add(nextNode);
    }

    public void addExecutedNode(NodeDefinition nodeDefinition) {
        executedNodes.add(nodeDefinition);
    }

    public List<NodeDefinition> getExecuteNodeList() {
        return executedNodes.toList();
    }
}
