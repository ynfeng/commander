package com.github.ynfeng.commander.server;

import com.google.common.collect.Lists;
import java.util.List;

public class StartSteps extends Steps {
    private final List<StartStep> steps = Lists.newArrayList();
    private final ShutdownSteps shutdownStpes = new ShutdownSteps();
    private static final CmderLogger LOG = CmderLoggerFactory.getServerLogger();

    public void addAll(List<StartStep> startStartSteps) {
        steps.addAll(startStartSteps);
    }

    public ShutdownSteps startup() {
        startupStepByStep();
        return shutdownStpes;
    }

    private void startupStepByStep() {
        steps.forEach(step -> checkedCall(
            () -> takeDuration(() -> shutdownStpes.add(new ShutdownStep(step.name(), step.execute()))))
            .onException(e -> LOG.info("Bootstrap {} [{}/{}] failed with unexpected exception.",
                step.name(), 1, steps.size(), e))
            .onResult(duration -> LOG.debug("Bootstrap [{}/{}]: {} started in {} ms", 1,
                steps.size(), step.name(), duration))
            .throwServerExceptionIfNecessary());
    }
}
