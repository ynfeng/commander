package com.github.commander.module;

public class TestModule extends AbstractModule<TestModuleConfig> {
    @Override
    public String name() {
        return "test";
    }

    @Override
    public void init(TestModuleConfig config) {
        registerComponent(TestComponent.class, new TestComponent());
        registerComponent(TestComponent1.class, new TestComponent1());
    }
}
