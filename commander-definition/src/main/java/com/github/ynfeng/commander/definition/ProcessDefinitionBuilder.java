package com.github.ynfeng.commander.definition;


public class ProcessDefinitionBuilder {
    private final ProcessDefinition processDefinition;
    private final NodeDefinitions nodeDefinitions = NodeDefinitions.create();

    private ProcessDefinitionBuilder(String name, int version) {
        processDefinition = new ProcessDefinition(name, version);
    }

    public static ProcessDefinitionBuilder create(String name, int version) {
        return new ProcessDefinitionBuilder(name, version);
    }

    public StartDefinition createStart() {
        StartDefinition startDefinition = new StartDefinition();
        nodeDefinitions.add(startDefinition);
        processDefinition.start(startDefinition);
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
        NodeDefinition source = nodeDefinitions.get(sourceRefName);
        NodeDefinition target = nodeDefinitions.get(targetRefName);
        source.next(target);
        return this;
    }

    public ProcessDefinition build() {
        return processDefinition;
    }
}
