package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.ServiceDefinition;
import com.github.ynfeng.commander.engine.ProcessInstance;

public enum ServiceNodeExecuteState {
    Created {
        @Override
        void accept(ProcessInstance processInstance, ServiceDefinition serviceDefinition) {
            processInstance.setNodeExecutingVariable(
                serviceDefinition.refName(), "state", Waiting);
        }
    },
    Done {
        @Override
        void accept(ProcessInstance processInstance, ServiceDefinition serviceDefinition) {

        }
    },
    Waiting {
        @Override
        void accept(ProcessInstance processInstance, ServiceDefinition serviceDefinition) {
            processInstance.setNodeExecutingVariable(serviceDefinition.refName(), "state", Done)
                .thenAccept(v -> {
                    processInstance.addReadyNode(serviceDefinition.next());
                    processInstance.nodeComplete(serviceDefinition);
                });
        }
    };

    abstract void accept(ProcessInstance processInstance, ServiceDefinition serviceDefinition);
}
