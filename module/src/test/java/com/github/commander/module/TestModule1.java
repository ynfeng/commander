package com.github.commander.module;

public class TestModule1 extends AbstractModule<TestModuleConfig> {
    @Override
    public String name() {
        return "test1";
    }

    @Override
    public void init(TestModuleConfig config) {
    }
}
