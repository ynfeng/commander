package com.github.ynfeng.commander.core.context;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "idVal")
public final class ProcessId {
    private String idVal;

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