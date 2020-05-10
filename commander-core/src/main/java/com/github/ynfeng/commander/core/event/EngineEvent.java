package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public interface EngineEvent extends Event {
    ProcessContext context();
}
