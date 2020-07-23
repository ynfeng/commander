package com.github.ynfeng.commander.core.definition;

public class ProcessDefinitionBuilder {
    private ProcessDefinition processDefinition;
    private final NodeDefinitions nodeDefinitions = NodeDefinitions.create();
    private String name;
    private int version;

    protected ProcessDefinitionBuilder() {
    }

    public ProcessDefinitionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProcessDefinitionBuilder withVersion(int version) {
        this.version = version;
        processDefinition = new ProcessDefinition(this.name, this.version);
        return this;
    }

    public StartDefinition createStart() {
        StartDefinition startDefinition = new StartDefinition();
        nodeDefinitions.add(startDefinition);
        processDefinition.firstNode(startDefinition);
        return startDefinition;
    }

    public EndDefinition createEnd(String refName) {
        EndDefinition endDefinition = new EndDefinition(refName);
        nodeDefinitions.add(endDefinition);
        return endDefinition;
    }

    public ServiceDefinition createService(String refName, ServiceCoordinate serviceCoordinate) {
        ServiceDefinition serviceDefinition = new ServiceDefinition(refName, serviceCoordinate);
        nodeDefinitions.add(serviceDefinition);
        return serviceDefinition;
    }

    public ProcessDefinitionBuilder link(String sourceRefName, String targetRefName) {
        NextableNodeDefinition source = nodeDefinitions.get(sourceRefName);
        NodeDefinition target = nodeDefinitions.get(targetRefName);
        source.next(target);
        return this;
    }

    public DecisionDefinition createDecision(String refName) {
        DecisionDefinition decisionDefinition = new DecisionDefinition(refName);
        nodeDefinitions.add(decisionDefinition);
        return decisionDefinition;
    }

    public ProcessDefinition build() {
        return processDefinition;
    }

    public ForkDefinition createFork(String refName) {
        ForkDefinition forkDefinition = new ForkDefinition(refName);
        nodeDefinitions.add(forkDefinition);
        return forkDefinition;
    }

    public JoinDefinition createJoin(String refName) {
        JoinDefinition joinDefinition = new JoinDefinition(refName, nodeDefinitions);
        nodeDefinitions.add(joinDefinition);
        return joinDefinition;
    }
}
