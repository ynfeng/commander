package com.github.ynfeng.commander.engine;

import java.util.Objects;

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

    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessId)) {
            return false;
        }
        ProcessId processId = (ProcessId) o;
        if (idVal == null) {
            return processId.idVal == null;
        }
        return Objects.equals(idVal, ((ProcessId) o).idVal);
    }

    @Override
    public int hashCode() {
        if (idVal == null) {
            return 0;
        }
        return idVal.hashCode();
    }
}
