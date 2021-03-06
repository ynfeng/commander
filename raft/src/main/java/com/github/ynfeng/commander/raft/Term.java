package com.github.ynfeng.commander.raft;

public class Term {
    private int curTerm;

    private Term() {
    }

    private Term(int curTerm) {
        this.curTerm = curTerm;
    }

    public Term nextTerm() {
        return new Term(curTerm + 1);
    }

    public static Term create(int t) {
        return new Term(t);
    }

    public boolean lessThan(Term term) {
        return curTerm < term.curTerm;
    }

    public boolean greaterThan(Term term) {
        return curTerm > term.curTerm;
    }

    public boolean lessOrEqual(Term term) {
        return curTerm <= term.curTerm;
    }

    public boolean greaterOrEqual(Term term) {
        return curTerm >= term.curTerm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Term term = (Term) o;

        return curTerm == term.curTerm;
    }

    @Override
    public int hashCode() {
        return curTerm;
    }

    @Override
    public String toString() {
        return "Term{"
            + "curTerm=" + curTerm
            + '}';
    }

    public int value() {
        return curTerm;
    }
}
