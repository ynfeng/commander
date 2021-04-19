package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.ServiceDefinition;
import com.github.ynfeng.commander.engine.ProcessInstance;

public enum ServiceNodeExecuteState {
    CREATED {
        @Override
        void accept(ProcessInstance processInstance, ServiceDefinition serviceDefinition) {
            processInstance.setNodeExecutingVariable(
                serviceDefinition.refName(), "state", WAITING);
        }
    },
    DONE {
        @Override
        void accept(ProcessInstance processInstance, ServiceDefinition serviceDefinition) {
            throw new UnsupportedOperationException();
        }
    },
    WAITING {
        @Override
        void accept(ProcessInstance processInstance, ServiceDefinition serviceDefinition) {
            processInstance.setNodeExecutingVariable(serviceDefinition.refName(), "state", DONE)
                .thenAccept(v -> {
                    processInstance.addReadyNode(serviceDefinition.next());
                    processInstance.nodeComplete(serviceDefinition);
                });
        }
    };

    abstract void accept(ProcessInstance processInstance, ServiceDefinition serviceDefinition);
}
