package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;

public class GetNodeExecutingVariableResponse {
    private NodeExecutingVariable variable;

    public GetNodeExecutingVariableResponse(NodeExecutingVariable variable) {
        this.variable = variable;
    }

    public NodeExecutingVariable variable() {
        return variable;
    }
}
