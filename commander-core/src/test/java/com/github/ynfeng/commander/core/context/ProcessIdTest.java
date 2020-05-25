package com.github.ynfeng.commander.core.context;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.context.ProcessId;
import org.junit.jupiter.api.Test;

class ProcessIdTest {
    @Test
    public void should_create_process_id(){
        ProcessId processId = ProcessId.of("anIdValue");

        assertThat(processId.toString(), is("anIdValue"));
    }
}
