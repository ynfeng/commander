package com.github.ynfeng.commander.definition;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class ForkBranchs {
    private final List<ForkBranch> branches = Lists.newArrayList();

    protected ForkBranchs() {
    }

    public Iterator<ForkBranch> iterator() {
        return branches.iterator();
    }

    public void add(ForkBranch forkBranch) {
        branches.add(forkBranch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ForkBranchs)) {
            return false;
        }

        ForkBranchs that = (ForkBranchs) o;

        return branches.equals(that.branches);
    }

    @Override
    public int hashCode() {
        return branches.hashCode();
    }
}
