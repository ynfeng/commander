package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProcessInstanceRuntimeContext {
    private final ReadyNodes readyNodes = new ReadyNodes();
    private final RunningNodes runningNodes = new RunningNodes();
    private final ExecutedNodes executedNodes = new ExecutedNodes();
    private final Map<String, UpdatableNodeExecutingVariable> nodeExecutingVariables = Maps.newConcurrentMap();

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

    public List<String> getExecuteNodeRefNames() {
        return getExecuteNodeList()
            .stream()
            .map(NodeDefinition::refName)
            .collect(Collectors.toList());
    }

    public void setNodeExecutingVariable(String refName, String key, Object val) {
        nodeExecutingVariables.get(refName).put(key, val);
    }

    public NodeExecutingVariable getNodeExecutingVariable(String refName) {
        return nodeExecutingVariables.computeIfAbsent(refName, k -> {
            return new UpdatableNodeExecutingVariable();
        });
    }
}
