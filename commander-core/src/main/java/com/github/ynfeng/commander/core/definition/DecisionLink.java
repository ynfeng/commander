package com.github.ynfeng.commander.core.definition;

public class DecisionLink implements Link {
    private final String decisionRefName;
    private final String exepression;
    private final String targetRefName;

    public DecisionLink(String decisionRefName, String exepression, String targetRefName) {
        this.decisionRefName = decisionRefName;
        this.exepression = exepression;
        this.targetRefName = targetRefName;
    }

    @Override
    public void doLink(NodeDefinitions nodeDefinitions) {
        DecisionDefinition decisionDefinition = nodeDefinitions.get(decisionRefName);
        decisionDefinition.condition(Expression.of(exepression), nodeDefinitions.get(targetRefName));
    }
}
