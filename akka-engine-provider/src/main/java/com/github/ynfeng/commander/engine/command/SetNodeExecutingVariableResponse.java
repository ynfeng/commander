package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;

public class SetNodeExecutingVariableResponse {
    private NodeExecutingVariable variable;

    public SetNodeExecutingVariableResponse(NodeExecutingVariable variable) {
        this.variable = variable;
    }

    public NodeExecutingVariable variable() {
        return variable;
    }
}
