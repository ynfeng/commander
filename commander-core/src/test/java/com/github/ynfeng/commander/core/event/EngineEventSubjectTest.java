package com.github.ynfeng.commander.core.event;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EngineEventSubjectTest {
    private ProcessContext processContext;

    @BeforeEach
    public void setUp() {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);
        processDefinition.firstNode(new StartDefinition());
        processContext = ProcessContext.create(ProcessId.of("id"), processDefinition);
        EngineEventSubject.getInstance().removeAllListeners();
    }

    @Test
    public void should_publish_process_start_event() {
        AtomicReference<Event> exceptedEvent = new AtomicReference<>();
        EngineEventSubject.getInstance().registerListener(new EventListener() {
            @Override
            public void onEvent(Event event) {
                exceptedEvent.set(event);
            }

            @Override
            public boolean interestedOn(Event event) {
                return event instanceof ProcessStartedEvent;
            }
        });
        EngineEventSubject.getInstance().notifyProcessStartedEvent();

        assertThat(exceptedEvent.get(), instanceOf(ProcessStartedEvent.class));
    }

    @Test
    public void should_publish_node_execute_complted_event() {
        AtomicReference<Event> exceptedEvent = new AtomicReference<>();
        EngineEventSubject.getInstance().registerListener(new EventListener() {
            @Override
            public void onEvent(Event event) {
                exceptedEvent.set(event);
            }

            @Override
            public boolean interestedOn(Event event) {
                return event instanceof NodeExecuteCompletedEvent;
            }
        });
        EngineEventSubject.getInstance().notifyNodeExecutedComplete();

        assertThat(exceptedEvent.get(), instanceOf(NodeExecuteCompletedEvent.class));
    }

    @Test
    public void should_publish_process_execute_complete_event() {
        AtomicReference<Event> exceptedEvent = new AtomicReference<>();
        EngineEventSubject.getInstance().registerListener(new EventListener() {
            @Override
            public void onEvent(Event event) {
                exceptedEvent.set(event);
            }

            @Override
            public boolean interestedOn(Event event) {
                return event instanceof ProcessExecuteCompletedEvent;
            }
        });
        EngineEventSubject.getInstance().notifyProcessExecutedComplete();

        assertThat(exceptedEvent.get(), instanceOf(ProcessExecuteCompletedEvent.class));
    }
}
