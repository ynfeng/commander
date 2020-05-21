package com.github.ynfeng.commander.core.context;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class ProcessFutureTest {

    @Test
    public void should_clear_listener_under_race_condition() throws InterruptedException {
        ProcessContext processContext = Mockito.mock(ProcessContext.class);
        AtomicInteger times = new AtomicInteger(0);
        Mockito.when(processContext.status()).thenAnswer(new Answer<ProcessStatus>() {
            @Override
            public ProcessStatus answer(InvocationOnMock invocation) throws Throwable {
                if(times.getAndIncrement() == 0){
                    return ProcessStatus.RUNNING;
                }
                return ProcessStatus.COMPLETED;
            }
        });
        ProcessFuture processFuture = ProcessFuture.create(processContext);

        processFuture.waitComplete();

        assertThat(EngineContext.numOfEventListeners(), is(0));
    }
}
