package com.github.ynfeng.commander.core.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.context.event.EngineEventSubject;
import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class ProcessFutureTest {
    private int times;

    @BeforeEach
    public void setup() {
        EngineEventSubject.getInstance().removeAllListeners();
    }

    @Test
    public void should_clear_listener_under_race_condition() throws InterruptedException {
        times = 0;
        ProcessContext processContext = Mockito.mock(ProcessContext.class);
        Mockito.when(processContext.status()).thenAnswer(new Answer<ProcessStatus>() {
            @Override
            public ProcessStatus answer(InvocationOnMock invocation) throws Throwable {
                if (times == 0) {
                    times++;
                    return ProcessStatus.RUNNING;
                }
                return ProcessStatus.COMPLETED;
            }
        });
        ProcessFuture processFuture = ProcessFuture.create(processContext);

        processFuture.sync();

        assertThat(EngineEventSubject.getInstance().numOfListeners(), is(0));
    }

    @Test
    public void should_return_immediately_when_process_already_complete() throws InterruptedException {
        ProcessContext processContext = Mockito.mock(ProcessContext.class);
        Mockito.when(processContext.status()).thenReturn(ProcessStatus.COMPLETED);
        ProcessFuture processFuture = ProcessFuture.create(processContext);

        processFuture.sync();
    }

    @Test
    public void should_return_immediately_when_process_already_exception() throws InterruptedException {
        ProcessContext processContext = Mockito.mock(ProcessContext.class);
        Mockito.when(processContext.status()).thenReturn(ProcessStatus.FAILED);
        ProcessFuture processFuture = ProcessFuture.create(processContext);

        processFuture.syncNotThrowException();
    }
}
