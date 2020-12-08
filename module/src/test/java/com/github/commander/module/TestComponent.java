package com.github.commander.module;

import com.github.ynfeng.commander.support.Named;

public class TestComponent implements Named {
    @Override
    public String name() {
        return "testComponent";
    }
}
