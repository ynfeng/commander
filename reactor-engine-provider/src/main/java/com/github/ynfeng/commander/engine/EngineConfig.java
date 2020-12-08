package com.github.ynfeng.commander.engine;

import com.github.commander.module.ComponentConfig;
import com.github.commander.module.ModuleConfig;

public class EngineConfig implements ModuleConfig {

    @Override
    public <C extends ComponentConfig> C getComponentConfig(String componentName) {
        return null;
    }
}
