package com.github.commander.module;

public class TestModule extends AbstractModule {
    @Override
    public String name() {
        return "test";
    }

    @Override
    public void init() {
        registerComponent(TestComponent.class, new TestComponent());
        registerComponent(TestComponent1.class, new TestComponent1());
    }
}
