package com.github.ynfeng.commander.server;

import com.google.common.collect.Lists;
import java.util.List;

public class ShutdownSteps extends Steps {
    private final List<ShutdownStep> steps = Lists.newArrayList();
    private static final CmderLogger LOG = CmderLoggerFactory.getServerLogger();
    private int currentStep = 1;

    public void add(ShutdownStep shutdownStep) {
        steps.add(shutdownStep);
    }

    public void shutdownStepByStep() {
        steps.stream().forEach(step -> executeStep(
            () -> takeDuration(step::execute))
            .onException(e -> LOG.info("Shutdown {} [{}/{}] failed with unexpected exception.",
                step.name(), currentStep++, steps.size(), e))
            .onResult(duration -> LOG.debug("Shutdown [{}/{}]: {} in {} ms", currentStep++,
                steps.size(), step.name(), duration)));
    }
}
