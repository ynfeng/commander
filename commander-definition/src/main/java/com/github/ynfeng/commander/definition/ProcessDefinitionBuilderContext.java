package com.github.ynfeng.commander.definition;

public class ProcessDefinitionBuilderContext {
    private static final ThreadLocal<ProcessDefinitionBuilderContext> CONTEXT_HOLDER
        = ThreadLocal.withInitial(() -> new ProcessDefinitionBuilderContext());

    private ProcessDefinition processDefinition;
    private NodeDefinition currentDefinition;

    private ProcessDefinitionBuilderContext() {
    }

    private static ProcessDefinitionBuilderContext context() {
        return CONTEXT_HOLDER.get();
    }

    protected static void processDefinition(ProcessDefinition processDefinition) {
        context().processDefinition = processDefinition;
    }

    protected static ProcessDefinition processDefinition() {
        return context().processDefinition;
    }

    public static void currentDefinition(NodeDefinition nodeDefinition) {
        context().currentDefinition = nodeDefinition;
    }

    public static NodeDefinition currentDefinition() {
        return context().currentDefinition;
    }
}
