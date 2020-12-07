package com.github.ynfeng.commander.bootstrap;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.github.ynfeng.commander.testtools.TestableLoggerAppender;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ShutdownStepsTest {

    private static String getLogMessage(int index) {
        List<LoggingEvent> events = TestableLoggerAppender.getEvents();
        LoggingEvent loggingEvent = events.get(index);
        return loggingEvent.getFormattedMessage();
    }

    @BeforeEach
    public void setup() {
        TestableLoggerAppender.reset();
    }

    @Test
    public void should_exeucte_shutdown_step() throws Exception {
        AutoCloseable closeable = Mockito.mock(AutoCloseable.class);
        ShutdownSteps shutdownSteps = new ShutdownSteps();
        shutdownSteps.add(new ShutdownStep("test", closeable));

        shutdownSteps.execute();

        Mockito.verify(closeable).close();
        assertThat(getLogMessage(0), containsString("Shutdown [1/1]: test in"));
        assertThat(getLogMessage(1), containsString("Shutdown succeeded. Shutdown 1 steps in"));
    }

    @Test
    public void should_log_infomation_when_occurs_exception_on_shutdown() throws Exception {
        AutoCloseable closeable = () -> {
            throw new RuntimeException();
        };
        ShutdownSteps shutdownSteps = new ShutdownSteps();
        shutdownSteps.add(new ShutdownStep("test", closeable));

        try {
            shutdownSteps.execute();
            fail("Should throw exception.");
        } catch (Exception e) {
        }

        assertThat(getLogMessage(0), is("Shutdown test [1/1] failed with unexpected exception."));
    }
}
