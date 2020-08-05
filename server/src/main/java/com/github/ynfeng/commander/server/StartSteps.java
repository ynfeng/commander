package com.github.ynfeng.commander.server;

import com.google.common.collect.Lists;
import java.util.List;

public class StartSteps {
    private final List<StartStep> startSteps = Lists.newArrayList();

    public void addAll(List<StartStep> startStartSteps) {
        startSteps.addAll(startStartSteps);
    }

    public ShutdownSteps startupStepByStep() {
        ShutdownSteps shutdownStpes = new ShutdownSteps();
        for (StartStep startStep : startSteps) {
            AutoCloseable closeable = startStep.execute();
            shutdownStpes.add(new ShutdownStep(startStep.name(), closeable));
        }
        return shutdownStpes;
    }
}
