package com.github.ynfeng.commander.core.definition;

public class DefaultDecisionLink implements Link {

    private final String decisionRefName;
    private final String targetRefName;

    public DefaultDecisionLink(String decisionRefName, String targetRefName) {
        this.decisionRefName = decisionRefName;
        this.targetRefName = targetRefName;
    }

    @Override
    public void doLink(NodeDefinitions nodeDefinitions) {
        DecisionDefinition decisionDefinition = nodeDefinitions.get(decisionRefName);
        decisionDefinition.defaultCondition(nodeDefinitions.get(targetRefName));
    }
}
