package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.ConditionBranch;
import com.github.ynfeng.commander.core.definition.ConditionBranches;
import com.github.ynfeng.commander.core.definition.DecisionDefinition;
import com.github.ynfeng.commander.core.definition.Expression;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.expression.ExepressionEvaluator;
import com.github.ynfeng.commander.core.expression.MVELExpressionEvaluator;
import java.util.Iterator;

public class DecisionNodeExecutor implements NodeExecutor {
    private final ExepressionEvaluator evaluator = new MVELExpressionEvaluator();

    @Override
    public void execute(ProcessContext processContext, NodeDefinition nodeDefinition) {
        DecisionDefinition decisionDefinition = (DecisionDefinition) nodeDefinition;
        final ConditionBranches branches = decisionDefinition.branches();
        if (executeBranches(processContext, nodeDefinition, branches)) {
            return;
        }
        executeDefaultBranch(processContext, nodeDefinition, decisionDefinition);
    }

    private boolean executeBranches(
        ProcessContext processContext, NodeDefinition nodeDefinition, ConditionBranches branches) {
        Iterator<ConditionBranch> it = branches.iterator();
        while (it.hasNext()) {
            ConditionBranch branch = it.next();
            if (executeBranch(processContext, nodeDefinition, branch)) {
                return true;
            }
        }
        return false;
    }

    private boolean executeBranch(
        ProcessContext processContext, NodeDefinition nodeDefinition, ConditionBranch branch) {
        Expression expression = branch.expression();
        boolean condition = evaluator.eval(expression.toString(), processContext);
        if (condition) {
            processContext.addReadyNode(branch.next());
            processContext.completeNode(nodeDefinition);
            return true;
        }
        return false;
    }

    private void executeDefaultBranch(
        ProcessContext processContext, NodeDefinition nodeDefinition, DecisionDefinition decisionDefinition) {
        ConditionBranch defaultBranch = decisionDefinition.defaultCondition();
        processContext.addReadyNode(defaultBranch.next());
        processContext.completeNode(nodeDefinition);
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof DecisionDefinition;
    }
}
