package com.github.ynfeng.commander.bootstrap;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.github.ynfeng.commander.testutil.TestableLoggerAppender;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class StartStepsTest {
    @BeforeEach
    public void setup() {
        TestableLoggerAppender.reset();
    }

    @Test
    public void should_execute_startup_steps() throws Exception {
        StartFunction startFunction = Mockito.mock(StartFunction.class);
        StartSteps startSteps = new StartSteps();
        startSteps.add(new StartStep("test", startFunction));

        startSteps.execute();

        Mockito.verify(startFunction).execute();
        assertThat(getLogMessage(0), containsString("Bootstrap [1/1]: test started in"));
        assertThat(getLogMessage(1), containsString("Bootstrap succeeded. Started 1 steps in"));
    }

    @Test
    public void should_log_infomation_when_occurs_exception_on_startup() throws Exception {
        StartSteps startSteps = new StartSteps();
        startSteps.add(new StartStep("test", () -> {
            throw new RuntimeException();
        }));

        try {
            startSteps.execute();
            fail("Should throw exception.");
        } catch (Exception e) {
            assertThat(e, notNullValue());
        }

        assertThat(getLogMessage(0), is("Bootstrap test [1/1] failed with unexpected exception."));
    }


    private static String getLogMessage(int index) {
        List<LoggingEvent> events = TestableLoggerAppender.getEvents();
        LoggingEvent loggingEvent = events.get(index);
        return loggingEvent.getFormattedMessage();
    }
}
