package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.collect.Lists;
import java.util.List;
import org.slf4j.Logger;

public class StartSteps extends Steps {
    private static final Logger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final List<StartStep> steps = Lists.newArrayList();
    private final ShutdownSteps shutdownStpes = new ShutdownSteps();
    private int currentStep = 1;

    public ShutdownSteps execute() throws Exception {
        long duration = takeDuration(this::startupStepByStep);
        LOGGER.debug(
            "Bootstrap succeeded. Started {} steps in {} ms.",
            steps.size(),
            duration);
        return shutdownStpes;
    }

    public void add(StartStep startStep) {
        steps.add(startStep);
    }

    private void startupStepByStep() {
        steps.forEach(step -> executeChecked(
            () -> takeDuration(() -> shutdownStpes.add(new ShutdownStep(step.name(), step.execute()))))
            .onException(e -> LOGGER.info("Bootstrap {} [{}/{}] failed with unexpected exception.",
                step.name(), currentStep++, steps.size(), e))
            .onResult(duration -> LOGGER.debug("Bootstrap [{}/{}]: {} started in {} ms.", currentStep++,
                steps.size(), step.name(), duration))
            .throwExceptionIfNecessary());
    }
}
