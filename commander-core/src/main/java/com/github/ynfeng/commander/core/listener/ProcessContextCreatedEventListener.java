package com.github.ynfeng.commander.core.listener;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.engine.ProcessInstance;
import com.github.ynfeng.commander.core.event.EngineEvent;
import com.github.ynfeng.commander.core.event.Event;
import com.github.ynfeng.commander.core.event.ProcessContextCreatedEvent;

public class ProcessContextCreatedEventListener implements EventListener {

    @Override
    public void onEvent(Event event) {
        ProcessContext context = ((ProcessContextCreatedEvent) event).context();
        ProcessInstance processInstance = ProcessInstance.create(context.processId(), context.processDefinition());
        context.attachProcessInstance(processInstance);
        processInstance.complete();
    }

    @Override
    public boolean interestedOn(Event event) {
        return event instanceof EngineEvent;
    }
}
