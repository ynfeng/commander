package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class ShutdownSteps extends Steps {
    private final List<ShutdownStep> steps = Lists.newArrayList();
    private static final CmderLogger LOG = CmderLoggerFactory.getServerLogger();
    private int currentStep = 1;

    public void add(ShutdownStep shutdownStep) {
        steps.add(shutdownStep);
    }

    public void execute() throws Exception {
        Collections.reverse(steps);
        long duration = takeDuration(this::shutdownStepByStep);
        LOG.debug(
            "Shutdown succeeded. Shutdown {} steps in {} ms.",
            steps.size(),
            duration);
    }

    public void shutdownStepByStep() {
        steps.stream().forEach(step -> executeChecked(
            () -> takeDuration(step::execute))
            .onException(e -> LOG.info("Shutdown {} [{}/{}] failed with unexpected exception.",
                step.name(), currentStep++, steps.size(), e))
            .throwExceptionIfNecessary()
            .onResult(duration -> LOG.debug("Shutdown [{}/{}]: {} in {} ms.", currentStep++,
                steps.size(), step.name(), duration)));
    }
}
