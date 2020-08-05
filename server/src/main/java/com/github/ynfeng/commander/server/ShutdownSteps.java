package com.github.ynfeng.commander.server;

import com.google.common.collect.Lists;
import java.util.List;

public class ShutdownSteps {
    private final List<ShutdownStep> steps = Lists.newArrayList();
    private static final CmderLogger LOG = CmderLoggerFactory.getSystemLogger();

    public void add(ShutdownStep shutdownStep) {
        steps.add(shutdownStep);
    }

    public void shutdownStepByStep() {
        for (ShutdownStep shutdownStep : steps) {
            try {
                shutdownStep.execute();
            } catch (Exception e) {
            }
        }
    }
}
