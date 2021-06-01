package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;

public class ShutdownSteps extends Steps {
    private static final Logger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final List<ShutdownStep> steps = Lists.newArrayList();
    private int currentStep = 1;

    public void add(ShutdownStep shutdownStep) {
        steps.add(shutdownStep);
    }

    public void execute() throws Exception {
        Collections.reverse(steps);
        long duration = takeDuration(this::shutdownStepByStep);
        LOGGER.debug(
            "Shutdown succeeded. Shutdown {} steps in {} ms.",
            steps.size(),
            duration);
    }

    public void shutdownStepByStep() {
        steps.stream().forEach(step -> executeChecked(
            () -> takeDuration(step::execute))
            .onException(e -> LOGGER.info("Shutdown {} [{}/{}] failed with unexpected exception.",
                step.name(), currentStep++, steps.size(), e))
            .throwExceptionIfNecessary()
            .onResult(duration -> LOGGER.debug("Shutdown [{}/{}]: {} in {} ms.", currentStep++,
                steps.size(), step.name(), duration)));
    }
}
