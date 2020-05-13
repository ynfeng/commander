package com.github.ynfeng.commander.core.engine;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.definition.FakeNodeDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.event.Event;
import com.github.ynfeng.commander.core.event.EventListener;
import com.github.ynfeng.commander.core.event.NodeExecuteCompleteEvent;
import com.github.ynfeng.commander.core.event.ProcessExecuteCompleteEvent;
import com.github.ynfeng.commander.core.event.ProcessStartEvent;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EngineContextTest {
    private ProcessContext processContext;

    @BeforeEach
    public void setUp() {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);
        processDefinition.firstNode(new FakeNodeDefinition("fake"));
        processContext = new ProcessContext(ProcessId.of("id"), processDefinition);
        EngineContext.destory();
    }

    @Test
    public void should_publish_process_start_event() {
        AtomicReference<Event> exceptedEvent = new AtomicReference<>();
        EngineContext.registerListener(new EventListener() {
            @Override
            public void onEvent(Event event) {
                exceptedEvent.set(event);
            }

            @Override
            public boolean interestedOn(Event event) {
                return event instanceof ProcessStartEvent;
            }
        });
        EngineContext.publishEvent(ProcessStartEvent.create(processContext));

        assertThat(exceptedEvent.get(), instanceOf(ProcessStartEvent.class));
    }

    @Test
    public void should_publish_node_execute_complted_event() {
        AtomicReference<Event> exceptedEvent = new AtomicReference<>();
        EngineContext.registerListener(new EventListener() {
            @Override
            public void onEvent(Event event) {
                exceptedEvent.set(event);
            }

            @Override
            public boolean interestedOn(Event event) {
                return event instanceof NodeExecuteCompleteEvent;
            }
        });
        EngineContext.publishEvent(NodeExecuteCompleteEvent.create(processContext));

        assertThat(exceptedEvent.get(), instanceOf(NodeExecuteCompleteEvent.class));
    }

    @Test
    public void should_publish_process_execute_complete_event() {
        AtomicReference<Event> exceptedEvent = new AtomicReference<>();
        EngineContext.registerListener(new EventListener() {
            @Override
            public void onEvent(Event event) {
                exceptedEvent.set(event);
            }

            @Override
            public boolean interestedOn(Event event) {
                return event instanceof ProcessExecuteCompleteEvent;
            }
        });
        EngineContext.publishEvent(ProcessExecuteCompleteEvent.create(processContext));

        assertThat(exceptedEvent.get(), instanceOf(ProcessExecuteCompleteEvent.class));
    }
}
