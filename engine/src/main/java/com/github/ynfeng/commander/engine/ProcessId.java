package com.github.ynfeng.commander.engine;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "idVal")
public final class ProcessId {
    private final String idVal;

    private ProcessId(String idVal) {
        this.idVal = idVal;
    }

    public static ProcessId of(String idVal) {
        return new ProcessId(idVal);
    }

    @Override
    public String toString() {
        return idVal;
    }
}
