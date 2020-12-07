package com.github.commander.module;

public class TestModuleConfig implements ModuleConfig {
    @Override
    public <C extends ComponentConfig> C getComponentConfig(String componentName) {
        return null;
    }
}
