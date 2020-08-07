package com.github.ynfeng.commander.server;

import com.google.common.collect.Lists;
import java.util.List;

public class ShutdownSteps extends Steps {
    private final List<ShutdownStep> steps = Lists.newArrayList();
    private static final CmderLogger LOG = CmderLoggerFactory.getServerLogger();

    public void add(ShutdownStep shutdownStep) {
        steps.add(shutdownStep);
    }

    public void shutdownStepByStep() {
        steps.stream().forEach(step -> executeStep(
            () -> takeDuration(step::execute))
            .onException(e -> LOG.info("Shutdown {} [{}/{}] failed with unexpected exception.",
                step.name(), 0 + 1, steps.size(), e))
            .onResult(duration -> LOG.debug("Shutdown [{}/{}]: {} in {} ms", 0 + 1,
                steps.size(), step.name(), duration)));
    }
}
