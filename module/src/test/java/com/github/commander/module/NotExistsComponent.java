package com.github.commander.module;

import com.github.ynfeng.commander.support.Named;

public class NotExistsComponent implements Named {
    @Override
    public String name() {
        return "none";
    }
}
