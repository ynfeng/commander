package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.ConditionBranch;
import com.github.ynfeng.commander.definition.ConditionBranches;
import com.github.ynfeng.commander.definition.DecisionDefinition;
import com.github.ynfeng.commander.definition.Expression;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.ProcessInstance;
import com.github.ynfeng.commander.engine.expression.ExepressionEvaluator;
import com.github.ynfeng.commander.engine.expression.MVELExpressionEvaluator;
import java.util.Iterator;

public class DecisionNodeExecutor implements NodeExecutor {
    private final ExepressionEvaluator evaluator = new MVELExpressionEvaluator();

    @Override
    public void execute(ProcessInstance processInstance, NodeDefinition nodeDefinition) {
        DecisionDefinition decisionDefinition = (DecisionDefinition) nodeDefinition;
        final ConditionBranches branches = decisionDefinition.branches();
        if (executeBranches(processInstance, nodeDefinition, branches)) {
            return;
        }
        executeDefaultBranch(processInstance, nodeDefinition, decisionDefinition);
    }

    private boolean executeBranches(
        ProcessInstance processInstance, NodeDefinition nodeDefinition, ConditionBranches branches) {
        Iterator<ConditionBranch> it = branches.iterator();
        while (it.hasNext()) {
            ConditionBranch branch = it.next();
            if (executeBranch(processInstance, nodeDefinition, branch)) {
                return true;
            }
        }
        return false;
    }

    private boolean executeBranch(
        ProcessInstance processInstance, NodeDefinition nodeDefinition, ConditionBranch branch) {
        Expression expression = branch.expression();
        boolean condition = evaluator.eval(expression.toString(), processInstance);
        if (condition) {
            processInstance.addReadyNode(branch.next());
            processInstance.nodeComplete(nodeDefinition);
            return true;
        }
        return false;
    }

    private void executeDefaultBranch(
        ProcessInstance processInstance, NodeDefinition nodeDefinition, DecisionDefinition decisionDefinition) {
        ConditionBranch defaultBranch = decisionDefinition.defaultCondition();
        processInstance.addReadyNode(defaultBranch.next());
        processInstance.nodeComplete(nodeDefinition);
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof DecisionDefinition;
    }
}
