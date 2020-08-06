package com.github.ynfeng.commander.server;

import com.google.common.collect.Lists;
import java.util.List;

public class ShutdownSteps {
    private final List<ShutdownStep> steps = Lists.newArrayList();
    private static final CmderLogger LOG = CmderLoggerFactory.getServerLogger();

    public void add(ShutdownStep shutdownStep) {
        steps.add(shutdownStep);
    }

    @SuppressWarnings("checkstyle:MethodLength")
    public void shutdownStepByStep() {
        for (int i = 0; i < steps.size(); i++) {
            ShutdownStep step = steps.get(i);
            try {
                step.execute();
            } catch (Exception e) {
                LOG.info(
                    "Shutdown {} [{}/{}] failed with unexpected exception.",
                    step.name(),
                    i + 1,
                    steps.size(),
                    e);
            }
        }
    }
}
